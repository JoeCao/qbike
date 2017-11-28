package club.newtech.qbike.trip.domain.core.root;

import club.newtech.qbike.trip.domain.core.Status;
import club.newtech.qbike.trip.domain.core.vo.Driver;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.EnumType.STRING;

@Data
@Entity
@Table(name = "t_driver_status")
public class DriverStatus {
    @Id
    private int dId;
    @Embedded
    private Driver driver;
    private Double currentLongitude;
    private Double currentLatitude;
    @Enumerated(value = STRING)
    @Column(length = 32, nullable = false)
    private Status status;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
}
