package club.newtech.qbike.intention.domain.core.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Embeddable
public class DriverVo {
    @Column(nullable = true)
    private int id;
    @Column(nullable = true)
    private String userName;
    @Column(nullable = true)
    private String mobile;
    @Column(nullable = true)
    private String type;
}
