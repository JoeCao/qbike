package club.newtech.qbike.trip.domain.core.root;

import club.newtech.qbike.trip.domain.core.Status;
import club.newtech.qbike.trip.domain.core.vo.Driver;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Data
@ToString
@Accessors(fluent = false, chain = true)
@Entity
public class Postion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tid;
    private Double positionLongitude;
    private Double positionLatitude;
    @Enumerated(value = STRING)
    @Column(length = 32, nullable = false)
    private Status status;
    @Embedded
    private Driver driver;

}
