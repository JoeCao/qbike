package club.newtech.qbike.intention;

import club.newtech.qbike.intention.domain.core.vo.Status;
import club.newtech.qbike.intention.domain.core.root.Intention;
import club.newtech.qbike.intention.domain.core.vo.Candidate;
import club.newtech.qbike.intention.domain.core.vo.Customer;
import club.newtech.qbike.intention.domain.repository.CandidateRepository;
import club.newtech.qbike.intention.domain.repository.IntentionRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntentionTest {
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    IntentionRepository intentionRepository;

    @Test
    public void saveIntention() {
        Intention intention = new Intention();
        intention.setStatus(Status.Inited);
        Customer customer = new Customer();
        customer.setCustomerId(1032);
        customer.setCustomerName("hello");
        customer.setCustomerMobile("13900001111");
        intention.setCustomer(customer);
        intentionRepository.save(intention);
        Candidate candidate = new Candidate();
        candidate.setIntention(intention);
        candidate.setDriverId(100);
        candidate.setCreated(new Date());

        candidateRepository.save(candidate);

        int id = intention.getMid();
        Intention another = intentionRepository.findOne(id);
        Assert.assertEquals(1, another.getCandidates().size());

    }
}
