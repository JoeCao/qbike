package club.newtech.qbike.order.domain.core.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 状态扭转 请求参数
 * Created by jinwei on 29/3/2017.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateRequest {

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 事件操作
     */
    private Events event;

    private Object data;
    /**
     * 用户信息
     */
    private int userId;

    /**
     * 追踪调用链
     */
    private String uId;

}
