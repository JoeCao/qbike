package club.newtech.qbike.intention.domain.core.vo;

import club.newtech.qbike.intention.domain.core.root.Intention;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "t_intention_candidate")
public class Candidate {
    /**
     * 按照DDD的理论，值对象是没有自己的主键的，应该用intentionId和driverId组成复合主键
     * 但是考虑JPA的实现难度和数据库管理的难度，所以这里做了一个反模式
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cid;
    @ManyToOne
    @JoinColumn(name = "intention_id")
    private Intention intention;
    private int driverId;
    private String driverName;
    private String driverMobile;
    private Double longitude;
    private Double latitude;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

}
