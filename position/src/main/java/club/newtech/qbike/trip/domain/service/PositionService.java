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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            driverStatus.setStatus(Status.ONLINE);
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
        String message = Stream.of(String.valueOf(driverId), String.valueOf(longitude), String.valueOf(latitude)).collect(Collectors.joining("|"));
        redisTemplate.convertAndSend("position", message);
        LOGGER.info("position send " + message);

    }
}
