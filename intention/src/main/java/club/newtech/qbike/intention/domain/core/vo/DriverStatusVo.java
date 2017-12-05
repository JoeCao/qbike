package club.newtech.qbike.intention.domain.core.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DriverStatusVo {
    private int dId;
    private DriverVo driver;
    private Double currentLongitude;
    private Double currentLatitude;
    private Date updateTime;
}
