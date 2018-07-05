package club.newtech.qbike.order.controller;

import club.newtech.qbike.order.domain.core.vo.Events;
import club.newtech.qbike.order.domain.core.vo.StateRequest;
import club.newtech.qbike.order.domain.exception.OrderRuntimeException;
import club.newtech.qbike.order.domain.repository.OrderRepository;
import club.newtech.qbike.order.domain.service.FsmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
public class OrderController {
    @Autowired
    FsmService fsmService;
    @Autowired
    OrderRepository orderRepository;

    @PostMapping("/order/cancel")
    public List<String> cancelOrder(int driverId, String orderId) {
        StateRequest stateRequest = new StateRequest();
        stateRequest.setEvent(Events.CANCEL);
        stateRequest.setData(orderRepository.findById(orderId).get());
        stateRequest.setUId(UUID.randomUUID().toString());
        stateRequest.setOrderId(orderId);
        stateRequest.setUserId(driverId);
        try {
            fsmService.changeState(stateRequest);
            return Arrays.asList("success", "");
        } catch (OrderRuntimeException oe) {
            return Arrays.asList(oe.getErrorCode(), oe.getErrorMessage());
        }
    }

    @PostMapping("/order/aboard")
    public List<String> aboard(int driverId, String orderId) {
        StateRequest stateRequest = new StateRequest();
        stateRequest.setEvent(Events.ABOARD);
        stateRequest.setData(orderRepository.findById(orderId).get());
        stateRequest.setUId(UUID.randomUUID().toString());
        stateRequest.setOrderId(orderId);
        stateRequest.setUserId(driverId);
        try {
            fsmService.changeState(stateRequest);
            return Arrays.asList("success", "");
        } catch (OrderRuntimeException oe) {
            return Arrays.asList(oe.getErrorCode(), oe.getErrorMessage());
        }
    }
}
