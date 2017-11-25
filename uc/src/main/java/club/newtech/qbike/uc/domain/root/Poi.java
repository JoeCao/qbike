package club.newtech.qbike.uc.domain.root;

import club.newtech.qbike.uc.domain.Type;
import lombok.Data;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "tb_poi")
@Data
public class Poi {
    @Id
    private int id;
    @Column(length = 64)
    private String linkMan;
    @Column(length = 64)
    private String shopName;
    @Column(length = 64, nullable = false)
    private String cellPhone;
    private Double longitude;
    private Double latitude;
    @Column(length = 64)
    private String province;
    @Column(length = 64)
    private String city;
    @Column(length = 64)
    private String district;
    private String street;
    private String streetNumber;
    private int shopType;
    private String userCode;
    private String originAddress;

}
