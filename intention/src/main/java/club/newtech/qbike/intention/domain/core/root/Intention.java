package club.newtech.qbike.intention.domain.core.root;

import club.newtech.qbike.intention.domain.core.vo.Candidate;
import club.newtech.qbike.intention.domain.core.vo.Customer;
import club.newtech.qbike.intention.domain.core.vo.DriverVo;
import club.newtech.qbike.intention.domain.core.vo.Status;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
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
    @Embedded
    private DriverVo selectedDriver;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated = new Date();

    @OneToMany(mappedBy = "intention", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Candidate> candidates = new ArrayList<>();

    public boolean canMatchDriver() {
        if (status.equals(Status.Inited)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean canConfirm() {
        if (status.equals(Status.UnConfirmed)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean waitingConfirm() {
        if (canMatchDriver()) {
            this.status = Status.UnConfirmed;
            this.updated = new Date();
            return true;
        } else {
            return false;
        }
    }

    public boolean fail() {
        if (this.status == Status.Inited) {
            this.status = Status.Failed;
            this.updated = new Date();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 抢单，先应答的司机抢单成功
     * 该方法线程不安全，请使用锁保证没有并发
     *
     * @param driverVo
     * @return 0 成功, -1 状态不对, -2 不是候选司机，-3 已被抢走
     */
    public int confirmIntention(DriverVo driverVo) {
        //判断状态
        if (!canConfirm()) {
            //状态不对
            return -1;
        }
        //判断是否是候选司机，不能随便什么司机都来抢单
        if (candidates.stream().map(Candidate::getDriverId).noneMatch(id -> id == driverVo.getId())) {
            return -2;
        }
        //将候选司机加入到列表中
        if (this.selectedDriver == null) {
            this.selectedDriver = driverVo;
            this.status = Status.Confirmed;
            this.updated = new Date();
            return 0;
        } else {
            return -3;
        }

    }
}
