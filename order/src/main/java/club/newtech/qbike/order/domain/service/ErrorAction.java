package club.newtech.qbike.order.domain.service;

import club.newtech.qbike.order.domain.core.vo.Events;
import club.newtech.qbike.order.domain.core.vo.FlowState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;

/**
 * 状态机异常Action
 *
 * @author wumeng[OF2627]
 * company qianmi.com
 * Date 2017-04-26
 */
@Service
public class ErrorAction implements Action<FlowState, Events> {

    @Override
    public void execute(StateContext context) {
        context.getExtendedState().getVariables().put(Exception.class, context.getException());
    }
}
