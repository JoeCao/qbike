package club.newtech.qbike.intention.domain.service;

import club.newtech.qbike.intention.domain.core.root.Intention;
import club.newtech.qbike.intention.domain.core.vo.Customer;
import club.newtech.qbike.intention.domain.repository.IntentionRepository;
import club.newtech.qbike.intention.infrastructure.UserRibbonHystrixApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntentionService {
    @Autowired
    IntentionRepository intentionRepository;
    @Autowired
    UserRibbonHystrixApi userApi;

    public void placeIntention(String userId, String myPoint, String destPoint) {
        Customer customer = userApi.findById(Integer.parseInt(userId));
        Intention intention = new Intention()
                .setDestPoint(destPoint)
                .setStartPoint(myPoint)
                .setCustomer(customer);
        intentionRepository.save(intention);

    }

}
