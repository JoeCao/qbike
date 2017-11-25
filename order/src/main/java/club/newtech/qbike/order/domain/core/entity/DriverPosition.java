package club.newtech.qbike.order.domain.core.entity;

import club.newtech.qbike.order.domain.core.vo.DriverVo;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class DriverPosition {
    @Id
    private int dId;
    @Embedded
    private DriverVo driverVo;
    private Double currentLongitude;
    private Double currentLatitude;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
}
