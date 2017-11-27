package club.newtech.qbike.intention.controller.bean;

import lombok.Data;

@Data
public class MyIntention {
    private int userId;
    private Double startLongitude;
    private Double startLatitude;
    private Double destLongitude;
    private Double destLatitude;

}
