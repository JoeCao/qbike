package club.newtech.qbike.trip.domain.service;

import club.newtech.qbike.trip.domain.core.Status;
import club.newtech.qbike.trip.domain.core.root.DriverStatus;
import club.newtech.qbike.trip.domain.core.vo.Driver;
import club.newtech.qbike.trip.domain.core.vo.Position;
import club.newtech.qbike.trip.domain.repository.DriverStatusRepo;
import club.newtech.qbike.trip.domain.repository.PositionRepository;
import club.newtech.qbike.trip.infrastructure.UserRibbonHystrixApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class PositionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PositionService.class);
    @Autowired
    DriverStatusRepo driverStatusRepo;
    @Autowired
    UserRibbonHystrixApi userService;
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    PositionRepository positionRepository;

    public void updatePosition(Integer driverId, Double longitude, Double latitude) {
        //先记录轨迹
        Date current = new Date();
        Position position = new Position();
        position.setDriverId(String.valueOf(driverId));
        position.setPositionLongitude(longitude);
        position.setPositionLatitude(latitude);
        //TODO 目前没有上传上下线状态
        position.setStatus(Status.ONLINE);
        position.setUploadTime(current);
        positionRepository.save(position);
        //更新状态表
        DriverStatus driverStatus = driverStatusRepo.findOne(driverId);
        if (driverStatus != null) {
            driverStatus.setCurrentLongitude(longitude);
            driverStatus.setCurrentLatitude(latitude);
            driverStatus.setUpdateTime(current);
            driverStatusRepo.save(driverStatus);
        } else {
            Driver driver = userService.findById(driverId);
            driverStatus = new DriverStatus();
            driverStatus.setDriver(driver);
            driverStatus.setCurrentLongitude(longitude);
            driverStatus.setCurrentLatitude(latitude);
            driverStatus.setUpdateTime(current);
            driverStatus.setStatus(Status.ONLINE);
            driverStatus.setDId(driverId);
            driverStatusRepo.save(driverStatus);
        }
        //更新Redis中的坐标数据，GeoHash
        redisTemplate.opsForGeo().geoAdd("Drivers", new Point(longitude, latitude), String.valueOf(driverId));
        LOGGER.info("position update " + driverStatus);

    }

    public Collection<DriverStatus> matchDriver(double longitude, double latitude) {
        Circle circle = new Circle(new Point(longitude, latitude), //
                new Distance(500, RedisGeoCommands.DistanceUnit.METERS));
        GeoResults<RedisGeoCommands.GeoLocation<String>> result =
                redisTemplate.opsForGeo().geoRadius("Drivers", circle);
        if (result.getContent().size() == 0) {
            LOGGER.info("没找到匹配司机");
            return new ArrayList<>();

        } else {
            List<String> drivers = result.getContent()
                    .stream()
                    .map(x -> x.getContent().getName())
                    .collect(toList());
            LOGGER.info("获取附近司机为{}", drivers);
            return drivers.stream().map(Integer::parseInt)
                    .map(id -> driverStatusRepo.findOne(id)).collect(toList());
        }
    }
}
