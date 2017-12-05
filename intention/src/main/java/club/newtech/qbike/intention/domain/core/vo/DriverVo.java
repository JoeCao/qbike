package club.newtech.qbike.intention.domain.core.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DriverVo {
    private int id;
    private String userName;
    private String mobile;
    private String type;
}
