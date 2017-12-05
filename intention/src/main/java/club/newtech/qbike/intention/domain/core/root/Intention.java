package club.newtech.qbike.intention.domain.core.root;

import club.newtech.qbike.intention.domain.Status;
import club.newtech.qbike.intention.domain.core.vo.Candidate;
import club.newtech.qbike.intention.domain.core.vo.Customer;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@Data
@ToString
@Accessors(fluent = false, chain = true)
@Entity
@Table(name = "t_intention")
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

    @OneToMany(mappedBy = "intention", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Candidate> candidates = new ArrayList<>();

    public boolean canMatchDriver() {
        if (status.equals(Status.Inited)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean canConfirm() {
        if (status.equals(Status.UnConfirmed)) {
            return true;
        } else {
            return false;
        }
    }
}
