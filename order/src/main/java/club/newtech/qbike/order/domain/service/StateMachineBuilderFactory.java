package club.newtech.qbike.order.domain.service;

import club.newtech.qbike.order.domain.core.root.Order;
import club.newtech.qbike.order.domain.core.vo.Events;
import club.newtech.qbike.order.domain.core.vo.FlowState;
import club.newtech.qbike.order.domain.exception.OrderRuntimeException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
public class StateMachineBuilderFactory {
    @Autowired
    private OrderStateMachineBuilder orderStateMachineBuilder;
    @Autowired
    private BeanFactory beanFactory;

    public StateMachine<FlowState, Events> create(Order order) {
        FlowState flowState = FlowState.forValue(order.getOrderStatus());

        try {
            StateMachine<FlowState, Events> sm = orderStateMachineBuilder.build(flowState, beanFactory);
//            sm.getStateMachineAccessor().withRegion().addStateMachineInterceptor(tradeCommonOptInterceptor);
            sm.start();
            sm.getExtendedState().getVariables().put(Order.class, order);
            return sm;
        } catch (Exception e) {
            throw new OrderRuntimeException(String.format("创建状态[%s]失败 => [%s]", flowState, e.getCause()));
        }

    }
}
