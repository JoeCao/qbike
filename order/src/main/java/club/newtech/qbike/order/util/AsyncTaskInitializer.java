package club.newtech.qbike.order.util;

import club.newtech.qbike.order.domain.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AsyncTaskInitializer {
    @Autowired
    OrderService orderService;

    @PostConstruct
    public void initialize() {
    }
}
