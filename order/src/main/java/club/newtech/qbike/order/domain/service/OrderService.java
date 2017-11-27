package club.newtech.qbike.order.domain.service;

import club.newtech.qbike.order.domain.core.entity.DriverPosition;
import club.newtech.qbike.order.domain.core.vo.DriverVo;
import club.newtech.qbike.order.domain.core.vo.IntentionVo;
import club.newtech.qbike.order.domain.repository.DriverPositionRepository;
import club.newtech.qbike.order.infrastructure.UserRibbonHystrixApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.DelayQueue;

@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    UserRibbonHystrixApi userService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private DelayQueue<IntentionVo> intentions = new DelayQueue<>();
    @Autowired
    private DriverPositionRepository driverPositionRepository;

    public void put(IntentionVo intentionVo) {
        LOGGER.info(String.format("put a vo %s to queue", intentionVo));
        this.intentions.put(intentionVo);
    }

    @Async
    public void handleIntention() {
        LOGGER.info("start handling intention add");
        for (; ; ) {
            try {
                IntentionVo intentionVo = intentions.take();
                if (intentionVo != null) {
                    LOGGER.info(String.format("get a vo %s", intentionVo));
                    //TODO match intention for trip
                    Circle circle = new Circle(new Point(intentionVo.getStartLong(), intentionVo.getStartLat()), //
                            new Distance(500, RedisGeoCommands.DistanceUnit.METERS));
                    GeoResults<RedisGeoCommands.GeoLocation<String>> result =
                            redisTemplate.opsForGeo().geoRadius("Drivers", circle);
                    LOGGER.info(result.toString());
                    if (result.getContent().size() == 0) {
                        LOGGER.info("没找到匹配, 放入队列重新等待");
                        IntentionVo newVo = new IntentionVo(intentionVo.getCustomerId(),
                                intentionVo.getStartLong(), intentionVo.getStartLat(),
                                intentionVo.getDestLong(), intentionVo.getDestLat(),
                                2000L);
                        intentions.put(newVo);
                    } else {
                        //TODO CREATE Order
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Transactional
    public void handlePosition(int driverId, double longitude, double latitude) {
        LOGGER.info("start handling position update");
        DriverVo driver = userService.findDriverById(driverId);
        if (driver != null) {
            DriverPosition dp = new DriverPosition();
            dp.setDriverVo(driver);
            dp.setCurrentLongitude(longitude);
            dp.setCurrentLatitude(latitude);
            dp.setDId(driverId);
            dp.setUpdateTime(new Date());
            driverPositionRepository.save(dp);

            redisTemplate.opsForGeo().geoAdd("Drivers", new Point(longitude, latitude), String.valueOf(driverId));
        }

    }

}
