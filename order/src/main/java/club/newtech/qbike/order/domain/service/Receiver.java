package club.newtech.qbike.order.domain.service;

import club.newtech.qbike.order.domain.core.vo.IntentionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Receiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);
    private OrderService orderService;

    public Receiver(OrderService taskService) {
        this.orderService = taskService;
    }

    public void receiveMessage(String message) {
        LOGGER.info("Received <" + message + ">");
        try {
            String[] values = message.split("\\|");
            IntentionVo intentionVo =
                    new IntentionVo(values[0], values[1], values[2], 2000L);

            orderService.put(intentionVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
