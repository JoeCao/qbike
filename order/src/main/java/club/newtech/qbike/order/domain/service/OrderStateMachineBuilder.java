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
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.transition.Transition;
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
                .states(EnumSet.allOf(FlowState.class));
        builder.configureTransitions()
                .withExternal()
                .source(FlowState.WAITING_ABOARD).target(FlowState.WAITING_ARRIVE)
                .event(Events.ABOARD)
                .action(aboard(), errorAction)
                .and()
                .withExternal()
                .source(FlowState.WAITING_ARRIVE).target(FlowState.UNPAY)
                .event(Events.ARRIVE)
                .and()
                .withExternal()
                .source(FlowState.UNPAY).target(FlowState.CLOSED)
                .event(Events.PAY);
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


    public StateMachineListener<FlowState, Events> listener() {
        return new StateMachineListenerAdapter<FlowState, Events>() {
            @Override
            public void transition(Transition<FlowState, Events> transition) {
                if (transition.getTarget().getId() == FlowState.WAITING_ABOARD) {
                    LOGGER.info("订单创建，等待用户上车");
                    return;
                }
                if (transition.getSource().getId() == FlowState.WAITING_ABOARD
                        && transition.getTarget().getId() == FlowState.WAITING_ARRIVE) {
                    LOGGER.info("用户已上车，等待到达目的地");
                    return;
                }
                if (transition.getSource().getId() == FlowState.WAITING_ARRIVE
                        && transition.getTarget().getId() == FlowState.UNPAY) {
                    LOGGER.info("用户已到达，等待支付");
                    return;
                }
                if (transition.getSource().getId() == FlowState.UNPAY
                        && transition.getTarget().getId() == FlowState.CLOSED) {
                    LOGGER.info("用户已支付");
                    return;
                }
            }
        };
    }
}
