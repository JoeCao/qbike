package club.newtech.qbike.intention.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AsyncTaskInitializer {
    @Autowired
    IntentionService intentionService;

    @PostConstruct
    public void initialize() {
        intentionService.handleTask();
    }
}
