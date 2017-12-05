package club.newtech.qbike.intention.controller.bean;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@ToString
@Data
@Accessors(fluent = false, chain = true)
public class IntentionVo {
    private int customerId;
    private double startLong;
    private double startLat;
    private double destLong;
    private double destLat;
    private int intentionId;
    private int driverId;
}
