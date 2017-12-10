package club.newtech.qbike.order.domain.service;

import club.newtech.qbike.order.domain.core.root.Order;
import club.newtech.qbike.order.domain.core.vo.Events;
import club.newtech.qbike.order.domain.core.vo.FlowState;
import club.newtech.qbike.order.domain.core.vo.StateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class OrderStateMachineBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStateMachineBuilder.class);
    @Autowired
    OrderService orderService;
    @Autowired
    ErrorAction errorAction;

    public StateMachine<FlowState, Events> build(FlowState initState, BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<FlowState, Events> builder = StateMachineBuilder.builder();
        builder.configureConfiguration()
                .withConfiguration()
                .machineId("orderFSM")
                .beanFactory(beanFactory);
        builder.configureStates()
                .withStates()
                .initial(initState)
                .end(FlowState.CLOSED)
                .end(FlowState.CANCELED)
                .states(EnumSet.allOf(FlowState.class));
        builder.configureTransitions()
                .withExternal()
                .source(FlowState.WAITING_ABOARD).target(FlowState.WAITING_ARRIVE)
                .event(Events.ABOARD)
                .action(aboard(), errorAction)
                //司机接单后，三分钟内可以无条件取消
                .and()
                .withExternal()
                .source(FlowState.WAITING_ABOARD).target(FlowState.CANCELED)
                .event(Events.CANCEL)
                .action(cancel(), errorAction)
                // 上车后，等待结束
                .and()
                .withExternal()
                .source(FlowState.WAITING_ARRIVE).target(FlowState.UNPAY)
                .event(Events.ARRIVE)
                .and()
                .withExternal()
                .source(FlowState.UNPAY).target(FlowState.PAYING)
                .event(Events.PAY)
                .and()
                .withExternal()
                .source(FlowState.PAYING).target(FlowState.WAITING_COMMENT)
                .event(Events.PAY_CALLBACK)
                .and()
                .withExternal()
                .source(FlowState.WAITING_COMMENT).target(FlowState.CLOSED)
                .event(Events.COMMENT);


        return builder.build();
    }

    public Action<FlowState, Events> aboard() {
        return stateContext -> {
            Order order = stateContext.getExtendedState().get(StateRequest.class, Order.class);
            String uuId = stateContext.getExtendedState().get(OrderService.UUID_KEY, String.class);
            order.setOrderStatus(FlowState.WAITING_ARRIVE.toValue());
            orderService.aboard(order);
        };
    }

    public Action<FlowState, Events> cancel() {
        return stateContext -> {
            Order order = stateContext.getExtendedState().get(StateRequest.class, Order.class);
            String uuId = stateContext.getExtendedState().get(OrderService.UUID_KEY, String.class);
            order.setOrderStatus(FlowState.CANCELED.toValue());
            orderService.cancel(order);
        };
    }

}
