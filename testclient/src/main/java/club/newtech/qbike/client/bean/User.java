package club.newtech.qbike.client.bean;

import lombok.Data;

@Data
public class User {

    private int id;
    private String userName;
    private String mobile;
    private String province;
    private String city;
    private String district;
    private String street;
    private String originAddress;
    private String type;

}