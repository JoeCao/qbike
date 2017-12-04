package club.newtech.qbike.order.domain.core.root;

import club.newtech.qbike.order.domain.core.vo.CustomerVo;
import club.newtech.qbike.order.domain.core.vo.DriverVo;
import club.newtech.qbike.order.domain.core.vo.Status;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Data
@ToString
@Accessors(fluent = false, chain = true)
@Entity
@Table(name = "t_qbike_order")
public class Order {
    @Id
    private String oid;
    @Embedded
    private CustomerVo customer;
    @Embedded
    private DriverVo driver;
    private Double startLong;
    private Double startLat;
    private Double destLong;
    private Double destLat;
    @Temporal(TemporalType.TIMESTAMP)
    private Date opened;
    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    private Status orderStatus;
    private String intentionId;
}
