package club.newtech.qbike.order.domain.service;

import club.newtech.qbike.order.domain.core.vo.IntentionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.DelayQueue;

@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private DelayQueue<IntentionVo> intentions = new DelayQueue<IntentionVo>();

    public void put(IntentionVo intentionVo) {
        LOGGER.info(String.format("put a vo %s to queue" , intentionVo));
        this.intentions.put(intentionVo);
    }

    @Async
    public void handle() {
        LOGGER.info("start handling");
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

}
