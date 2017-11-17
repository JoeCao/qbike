package club.newtech.qbike.uc.domain.root;

import club.newtech.qbike.uc.domain.Type;
import lombok.Data;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 64)
    private String name;
    @Column(length = 64, nullable = false)
    private String mobile;

    @Enumerated(value = STRING)
    @Column(length = 32, nullable = false)
    private Type type;

}
