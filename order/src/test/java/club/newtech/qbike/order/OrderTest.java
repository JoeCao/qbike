package club.newtech.qbike.order;

import club.newtech.qbike.order.domain.core.root.Order;
import club.newtech.qbike.order.domain.core.vo.Events;
import club.newtech.qbike.order.domain.core.vo.IntentionVo;
import club.newtech.qbike.order.domain.core.vo.StateRequest;
import club.newtech.qbike.order.domain.repository.OrderRepository;
import club.newtech.qbike.order.domain.service.FsmService;
import club.newtech.qbike.order.domain.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {
    @Autowired
    FsmService fsmService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void changeState() {

        Order order = orderRepository.findById("T0000000005").orElse(null);
        StateRequest stateRequest = new StateRequest();
        stateRequest.setOrderId(order.getOid());
        stateRequest.setUId(UUID.randomUUID().toString());
        stateRequest.setUserId(96);
        stateRequest.setData(order);
        stateRequest.setEvent(Events.ABOARD);
        fsmService.changeState(stateRequest);

    }
    @Test
    public void createAndChange() {
        IntentionVo intentionVo = new IntentionVo();
        intentionVo.setCustomerId(84);
        intentionVo.setDriverId(96);
        intentionVo.setIntentionId(1);
        intentionVo.setStartLong(118.71334176065);
        intentionVo.setStartLat(32.012518207527);
        intentionVo.setDestLong(118.81722069709);
        intentionVo.setDestLat(32.007969136143);
        Order order = orderService.createOrder(intentionVo);
        StateRequest stateRequest = new StateRequest();
        stateRequest.setOrderId(order.getOid());
        stateRequest.setUId(UUID.randomUUID().toString());
        stateRequest.setUserId(96);
        stateRequest.setData(order);
        stateRequest.setEvent(Events.ABOARD);
        fsmService.changeState(stateRequest);

    }
}
