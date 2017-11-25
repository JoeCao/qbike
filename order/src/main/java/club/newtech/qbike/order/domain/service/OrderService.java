package club.newtech.qbike.order.domain.service;

import club.newtech.qbike.order.domain.core.entity.DriverPosition;
import club.newtech.qbike.order.domain.core.vo.DriverVo;
import club.newtech.qbike.order.domain.core.vo.IntentionVo;
import club.newtech.qbike.order.domain.repository.DriverPositionRepository;
import club.newtech.qbike.order.infrastructure.UserRibbonHystrixApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.DelayQueue;

@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    UserRibbonHystrixApi userService;
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
                    //TODO CREATE Order
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void handlePosition(int driverId, String position) {
        LOGGER.info("start handling position update");
        DriverVo driver = userService.findDriverById(driverId);
        if (driver != null) {
            DriverPosition dp = new DriverPosition();
            dp.setDriverVo(driver);
            dp.setCurrentPoint(position);
            dp.setDId(driverId);
            dp.setUpdateTime(new Date());
            driverPositionRepository.save(dp);
        }

    }

}
