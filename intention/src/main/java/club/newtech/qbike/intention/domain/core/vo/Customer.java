package club.newtech.qbike.intention.domain.core.vo;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class Customer {
    private int id;
    private String name;
    private String mobile;
}
