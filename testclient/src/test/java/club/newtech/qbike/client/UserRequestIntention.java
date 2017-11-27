package club.newtech.qbike.client;

import club.newtech.qbike.client.api.UserApi;
import club.newtech.qbike.client.bean.MyIntention;
import club.newtech.qbike.client.bean.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRequestIntention {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserApi userApi;

    @Test
    public void myNewIntention() {
        MyIntention myIntention = new MyIntention();
        myIntention.setUserId(1270);
        myIntention.setStartLongitude(118.81722069709);
        myIntention.setStartLatitude(32.007969136143);
        myIntention.setDestLongitude(118.71334176065);
        myIntention.setDestLatitude(32.012518207527);
//        restTemplate.postForObject("http://qbike-intention/intentions/place", myIntention, MyIntention.class);
        userApi.newIntention(myIntention);
    }


    @Test
    public void testNewUser() {
        User user = new User();
        user.setUserName("Joe");
        user.setMobile("13900001111");
        user.setType("Customer");
        userApi.newUser(user);
    }
}
