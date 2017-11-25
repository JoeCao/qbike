package club.newtech.qbike.trip.domain.core.vo;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class Driver {
    private int id;
    private String userName;
    private String mobile;
    private String type;
}
