package club.newtech.qbike.domain.core.vo;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class User {
    private int id;
    private String name;
    private String mobile;
}
