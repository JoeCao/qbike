package club.newtech.qbike.order.domain.core.vo;

import java.util.HashMap;
import java.util.Map;

public enum FlowState {
    WAITING_ABOARD("WAITING_ABOARD", "等待上车"),
    WAITING_ARRIVE("WAITING_ARRIVE", "等待到达目的地"),
    UNPAY("UNPAY", "等待支付"),
    PAYING("PAYING", "已支付待确认"),
    WAITING_COMMENT("WAITING_COMMENT", "等待评论"),
    CLOSED("CLOSED", "订单关闭"),
    CANCELED("CANCELED", "订单取消");
    private static Map<String, FlowState> flowStateMap = new HashMap<>();

    static {
        flowStateMap.put(WAITING_ABOARD.getStateId(), WAITING_ABOARD);
        flowStateMap.put(WAITING_ARRIVE.getStateId(), WAITING_ARRIVE);
        flowStateMap.put(UNPAY.getStateId(), UNPAY);
        flowStateMap.put(PAYING.getStateId(), PAYING);
        flowStateMap.put(WAITING_COMMENT.getStateId(), WAITING_COMMENT);
        flowStateMap.put(CLOSED.getStateId(), CLOSED);
        flowStateMap.put(CANCELED.getStateId(), CANCELED);
    }

    private String stateId;
    private String description;

    FlowState(String stateId, String description) {
        this.stateId = stateId;
        this.description = description;
    }

    public static FlowState forValue(String stateId) {
        return flowStateMap.get(stateId);
    }

    public String toValue() {
        return this.getStateId();
    }

    public String getStateId() {
        return stateId;
    }

    public String getDescription() {
        return description;
    }
}
