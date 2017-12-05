package club.newtech.qbike.intention.domain.service;

import club.newtech.qbike.intention.controller.bean.IntentionVo;
import club.newtech.qbike.intention.domain.core.root.Intention;
import club.newtech.qbike.intention.domain.core.vo.*;
import club.newtech.qbike.intention.domain.exception.LockException;
import club.newtech.qbike.intention.domain.repository.CandidateRepository;
import club.newtech.qbike.intention.domain.repository.IntentionRepository;
import club.newtech.qbike.intention.infrastructure.PositionApi;
import club.newtech.qbike.intention.infrastructure.UserRibbonHystrixApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

@Service
public class IntentionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntentionService.class);
    @Autowired
    IntentionRepository intentionRepository;
    @Autowired
    UserRibbonHystrixApi userApi;
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    PositionApi positionApi;
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    LockService lockService;
    @Autowired
    ObjectMapper objectMapper;


    private DelayQueue<IntentionTask> intentions = new DelayQueue<>();

    private static Candidate fromDriverStatus(DriverStatusVo driverStatusVo, Intention intention) {
        Candidate candidate = new Candidate();
        candidate.setDriverId(driverStatusVo.getDId());
        candidate.setCreated(new Date());
        candidate.setDriverMobile(driverStatusVo.getDriver().getMobile());
        candidate.setDriverName(driverStatusVo.getDriver().getUserName());
        candidate.setLongitude(driverStatusVo.getCurrentLongitude());
        candidate.setLatitude(driverStatusVo.getCurrentLatitude());
        candidate.setIntention(intention);
        return candidate;
    }

    @Transactional
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
        IntentionTask task = new IntentionTask(intention.getMid(), 2L, TimeUnit.SECONDS, 0);

        intentions.put(task);
    }

    @Transactional
    public void sendNotification(Collection<DriverStatusVo> result, Intention intention) {
        result.stream()
                .map(vo -> fromDriverStatus(vo, intention))
                .forEach(candidate -> candidateRepository.save(candidate));
        intention.waitingConfirm();
        intentionRepository.save(intention);
    }

    @Transactional
    public boolean confirmIntention(int driverId, int intentionId) {
        String lockName = "intention" + intentionId;
        Lock lock = null;
        try {
            lock = lockService.create(lockName);
            Intention intention = intentionRepository.findOne(intentionId);
            DriverVo driverVo = userApi.findDriverById(driverId);
            int ret = intention.confirmIntention(driverVo);
            LOGGER.info("{}司机抢单{}结果为{}", driverId, intentionId, ret);
            if (ret == 0) {
                intentionRepository.save(intention);
                IntentionVo intentionVo = new IntentionVo().setIntentionId(intention.getMid())
                        .setCustomerId(intention.getCustomer().getCustomerId())
                        .setDestLat(intention.getDestLatitude())
                        .setDestLong(intention.getDestLongitude())
                        .setStartLong(intention.getStartLongitude())
                        .setStartLat(intention.getStartLatitude())
                        .setDriverId(intention.getSelectedDriver().getId());
                try {
                    redisTemplate.convertAndSend("intention", objectMapper.writeValueAsString(intentionVo));
                } catch (JsonProcessingException e) {
                    LOGGER.error("convert message fail" + intentionVo, e);
                }
                return true;
            } else {
                return false;
            }
        } catch (LockException e) {
            LOGGER.error("try lock error ", e);
            return false;
        } finally {
            if (lock != null) {
                lockService.release(lockName, lock.getValue());
            }
        }
    }

    @Transactional
    public void matchFail(Intention intention) {
        intention.fail();
        intentionRepository.save(intention);
    }

    @Async
    public void handleTask() {
        LOGGER.info("start handling intention task loop");
        for (; ; ) {
            try {
                IntentionTask task = intentions.take();
                if (task != null) {
                    LOGGER.info("got a task {}", task.getIntenionId());
                    Intention intention = intentionRepository.findOne(task.getIntenionId());
                    if (intention.canMatchDriver()) {
                        //调用position服务匹配司机
                        Collection<DriverStatusVo> result = positionApi.match(intention.getStartLongitude(), intention.getStartLatitude());
                        if (result.size() > 0) {
                            List<String> names = result.stream().map(s -> s.getDriver().getUserName()).collect(toList());
                            LOGGER.info("匹配司机{}，将向他们发送抢单请求", names);
                            sendNotification(result, intention);
                        } else {
                            LOGGER.info("没有匹配到司机，放入队列继续等待");
                            retryMatch(task, intention);
                        }
                    } else {
                        // 忽略
                    }

                }
            } catch (Exception e) {
                LOGGER.error("error happened", e);
            }
        }
    }

    private boolean retryMatch(IntentionTask task, Intention intention) {
        int times = task.getRepeatTimes() + 1;
        LOGGER.info("task 已经被循环{} 次", times);
        if (times > 5) {
            //超过n次匹配无法匹配，就失败
            matchFail(intention);
            return true;
        }
        IntentionTask newTask = new IntentionTask(intention.getMid(), 2 * times, TimeUnit.SECONDS, times);
        this.intentions.put(newTask);
        return false;
    }
}
