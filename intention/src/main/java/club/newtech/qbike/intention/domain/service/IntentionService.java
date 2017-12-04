package club.newtech.qbike.intention.domain.service;

import club.newtech.qbike.intention.domain.Status;
import club.newtech.qbike.intention.domain.core.root.Intention;
import club.newtech.qbike.intention.domain.core.vo.Customer;
import club.newtech.qbike.intention.domain.core.vo.IntentionTask;
import club.newtech.qbike.intention.domain.repository.IntentionRepository;
import club.newtech.qbike.intention.infrastructure.UserRibbonHystrixApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.DelayQueue;

@Service
public class IntentionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntentionService.class);
    @Autowired
    IntentionRepository intentionRepository;
    @Autowired
    UserRibbonHystrixApi userApi;
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    private DelayQueue<IntentionTask> intentions = new DelayQueue<>();


    public void placeIntention(int userId, Double startLongitude, Double startLatitude,
                               Double destLongitude, Double destLatitude) {
        Customer customer = userApi.findById(userId);
        Intention intention = new Intention()
                .setStartLongitude(startLongitude)
                .setStartLatitude(startLatitude)
                .setDestLongitude(destLongitude)
                .setDestLatitude(destLatitude)
                .setCustomer(customer)
                .setStatus(Status.Inited);
        intentionRepository.save(intention);
//        String message = Stream.of(userId,
//                startLongitude, startLatitude,
//                destLongitude, destLatitude,
//                intention.getMid()).map(String::valueOf).collect(Collectors.joining("|"));
//        redisTemplate.convertAndSend("intention", message);
        IntentionTask task = new IntentionTask(intention.getMid(), 2000L);

        intentions.put(task);
    }

    @Async
    public void handleTask() {
        LOGGER.info("start handling intention task loop");
        for (; ; ) {
            try {
                IntentionTask task = intentions.take();
                if (task != null) {
                    Intention intention = intentionRepository.findOne(task.getIntenionId());
                    if (intention.canMatchDriver()) {
                        //TODO 调用position服务匹配司机
                    } else {
                        // 忽略
                    }

                }
            } catch (Exception e) {
                LOGGER.error("error happened", e);
            }
        }
    }
}
