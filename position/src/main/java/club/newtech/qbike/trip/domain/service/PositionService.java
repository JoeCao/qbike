package club.newtech.qbike.trip.domain.service;

import club.newtech.qbike.trip.domain.core.Status;
import club.newtech.qbike.trip.domain.core.root.Postion;
import club.newtech.qbike.trip.domain.core.vo.Driver;
import club.newtech.qbike.trip.domain.repository.PositionRepository;
import club.newtech.qbike.trip.infrastructure.UserRibbonHystrixApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class PositionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PositionService.class);
    @Autowired
    PositionRepository positionRepository;
    @Autowired
    UserRibbonHystrixApi userService;
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    public void updatePosition(Integer driverId, Double longitude, Double latitude) {
        Postion trip = positionRepository.findByDriver_Id(driverId);
        if (trip != null) {
            trip.setPositionLongitude(longitude);
            trip.setPositionLatitude(latitude);
            positionRepository.save(trip);
        } else {
            Driver driver = userService.findById(driverId);
            trip = new Postion();
            trip.setDriver(driver);
            trip.setPositionLongitude(longitude);
            trip.setPositionLatitude(latitude);
            trip.setStatus(Status.ONLINE);
            positionRepository.save(trip);
        }
        String message = Stream.of(String.valueOf(driverId), String.valueOf(longitude), String.valueOf(latitude)).collect(Collectors.joining("|"));
        redisTemplate.convertAndSend("position", message);
        LOGGER.info("position send " + message);

    }
}
