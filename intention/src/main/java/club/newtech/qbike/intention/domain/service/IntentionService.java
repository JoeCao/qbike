package club.newtech.qbike.intention.domain.service;

import club.newtech.qbike.intention.domain.Status;
import club.newtech.qbike.intention.domain.core.root.Intention;
import club.newtech.qbike.intention.domain.core.vo.Customer;
import club.newtech.qbike.intention.domain.repository.IntentionRepository;
import club.newtech.qbike.intention.infrastructure.UserRibbonHystrixApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class IntentionService {
    @Autowired
    IntentionRepository intentionRepository;
    @Autowired
    UserRibbonHystrixApi userApi;
    @Autowired
    RedisTemplate<String, String> redisTemplate;

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
        String message = Stream.of(userId,
                startLongitude, startLatitude,
                destLongitude, destLatitude).map(String::valueOf).collect(Collectors.joining("|"));
        redisTemplate.convertAndSend("intention", message);

    }

}
