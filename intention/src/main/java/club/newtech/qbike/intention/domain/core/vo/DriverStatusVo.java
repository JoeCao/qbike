package club.newtech.qbike.intention.domain.core.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Embeddable;
import java.util.Date;

/**
 * 和Position服务中的DriverStatus内容一样
 * 这里的值对象的目的是避免共享带来的耦合
 * 让intention成为自治的服务
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DriverStatusVo {
    private int dId;
    private DriverVo driver;
    private Double currentLongitude;
    private Double currentLatitude;
    private Date updateTime;
}
