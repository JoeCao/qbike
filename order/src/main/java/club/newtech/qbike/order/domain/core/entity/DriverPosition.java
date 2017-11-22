package club.newtech.qbike.order.domain.core.entity;

import club.newtech.qbike.order.domain.core.vo.DriverVo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class DriverPosition {
    @Id
    private int id;
    @Embedded
    private DriverVo driverVo;
    @Column(length = 64)
    private String currentPoint;
}
