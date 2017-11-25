package club.newtech.qbike.uc;

import club.newtech.qbike.uc.domain.Type;
import club.newtech.qbike.uc.domain.repository.PoiRepository;
import club.newtech.qbike.uc.domain.repository.UserRepository;
import club.newtech.qbike.uc.domain.root.Poi;
import club.newtech.qbike.uc.domain.root.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TableTransfer {
    @Autowired
    PoiRepository poiRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @Rollback(false)
    public void transfer() {
        List<Poi> pois = poiRepository.findAll();
        pois.forEach(poi -> {
            User user = new User();
            user.setId(poi.getId());
            String phone = poi.getCellPhone();
            if (phone.contains(".")) {
                phone = phone.substring(0, phone.indexOf(".")+1);
            }
            user.setMobile(poi.getCellPhone());
            user.setType(Type.Driver);
            user.setUserName(poi.getLinkMan());
            user.setCity(poi.getCity());
            user.setDistrict(poi.getDistrict());
            user.setOriginAddress(poi.getOriginAddress());
            user.setProvince(poi.getProvince());
            user.setStreet(poi.getStreet());
            System.out.println(user);
            userRepository.save(user);
        });
    }
}
