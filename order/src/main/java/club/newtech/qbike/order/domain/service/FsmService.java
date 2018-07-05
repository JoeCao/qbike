package club.newtech.qbike.order.domain.service;

import club.newtech.qbike.order.domain.core.root.Order;
import club.newtech.qbike.order.domain.core.vo.Events;
import club.newtech.qbike.order.domain.core.vo.FlowState;
import club.newtech.qbike.order.domain.core.vo.StateRequest;
import club.newtech.qbike.order.domain.exception.OrderRuntimeException;
import club.newtech.qbike.order.domain.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

@Service
public class FsmService {

    public static final String UUID_KEY = "UUID_KEY";
    private static final Logger LOGGER = LoggerFactory.getLogger(FsmService.class);
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private StateMachineBuilderFactory builderFactory;

    /**
     * 修改订单状态
     * extendedState中存放有两个对象，key为StateRequest.class为最新获得的request数据，key为Order.class的是已有订单数据
     *
     * @param request 状态迁移请求
     * @return
     */
    public void changeState(StateRequest request) {
        //1、查找订单信息
        Order order = orderRepository.findById(request.getOrderId()).orElse(null);
        if (order == null) {
            throw new OrderRuntimeException("030001", new Object[]{request.getOrderId()});
        }


        //2. 根据订单创建状态机
        StateMachine<FlowState, Events> stateMachine = builderFactory.create(order);
        if (request.getData() != null) {
            stateMachine.getExtendedState().getVariables().put(StateRequest.class, request.getData());
        }
        //上下文 调用链UUID
        stateMachine.getExtendedState().getVariables().put(UUID_KEY, request.getUId());

        //3. 发送当前请求的状态
        boolean isSend = stateMachine.sendEvent(request.getEvent());

        if (!isSend) {
            LOGGER.error(String.format("无法从状态[%s]转向 => [%s]", FlowState.forValue(order.getOrderStatus()), request.getEvent()));
            throw new OrderRuntimeException("010003");
        }

        Exception error = stateMachine.getExtendedState().get(Exception.class, Exception.class);
        if (error != null) {
            if (error.getClass().equals(OrderRuntimeException.class)) {
                throw (OrderRuntimeException) error;
            } else {
                throw new OrderRuntimeException("000000", error);
            }
        }
    }
}
