package club.newtech.qbike.order.domain.core.vo;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class DriverVo {
    private int driverId;
    private String driverName;
    private String driverMobile;
}
