package club.newtech.qbike.intention.domain.core.root;

import club.newtech.qbike.intention.domain.Status;
import club.newtech.qbike.intention.domain.core.vo.Customer;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Data
@ToString
@Accessors(fluent = false, chain = true)
@Entity
public class Intention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mid;
    private Double startLongitude;
    private Double startLatitude;
    private Double destLongitude;
    private Double destLatitude;
    @Embedded
    private Customer customer;
    @Enumerated(value = STRING)
    @Column(length = 32, nullable = false)
    private Status status;
}
