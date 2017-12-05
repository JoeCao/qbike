package club.newtech.qbike.intention.domain.core.vo;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class Customer {
    private int customerId;
    private String customerName;
    private String customerMobile;
    private String userType;
}
