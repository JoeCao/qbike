package club.newtech.qbike.intention.controller;

import club.newtech.qbike.intention.domain.core.vo.Customer;
import club.newtech.qbike.intention.domain.service.IntentionService;
import club.newtech.qbike.intention.infrastructure.UserRibbonHystrixApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RibbonHystrixController {
    @Autowired
    private UserRibbonHystrixApi userRibbonHystrixApi;
    @Autowired
    private IntentionService intentionService;

    @GetMapping("/ribbon/{id}")
    public Customer findById(@PathVariable Integer id) {
        return this.userRibbonHystrixApi.findById(id);
    }

    @PostMapping("/intentions/place")
    public void place(String userId, String myPoint, String dest) {
        intentionService.placeIntention(userId, myPoint, dest);
    }
}
