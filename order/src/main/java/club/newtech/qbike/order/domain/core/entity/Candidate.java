package club.newtech.qbike.order.domain.core.entity;

import club.newtech.qbike.order.domain.core.root.Order;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "t_candidate")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cid;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private String candidateDriverId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
}
