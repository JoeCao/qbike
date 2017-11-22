package club.newtech.qbike.order.domain.core.vo;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class CustomerVo {
    private int customerId;
    private String customerName;
    private String customerMobile;
}
