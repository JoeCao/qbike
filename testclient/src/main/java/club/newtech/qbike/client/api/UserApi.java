package club.newtech.qbike.client.api;

import club.newtech.qbike.client.bean.MyIntention;
import club.newtech.qbike.client.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserApi {
    @Autowired
    RestTemplate restTemplate;

    public void newUser(User user) {
        restTemplate.postForObject("http://qbike-uc/users", user, User.class);
    }

    public void newIntention(MyIntention myIntention) {
        restTemplate.postForObject("http://qbike-intention/intentions/place", myIntention, MyIntention.class);
    }
}
