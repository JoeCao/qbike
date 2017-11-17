package club.newtech.qbike.domain.core.service;

import club.newtech.qbike.domain.core.vo.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserRibbonHystrixService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRibbonHystrixService.class);
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 使用@HystrixCommand注解指定当该方法发生异常时调用的方法
     *
     * @param id id
     * @return 通过id查询到的用户
     */
    @HystrixCommand(fallbackMethod = "fallback")
    public User findById(Integer id) {
        return this.restTemplate.getForObject("http://QBIKE-UC/users/" + id, User.class);
    }

    /**
     * hystrix fallback方法
     *
     * @param id id
     * @return 默认的用户
     */
    public User fallback(Integer id) {
        UserRibbonHystrixService.LOGGER.info("异常发生，进入fallback方法，接收的参数：id = {}", id);
        User user = new User();
        user.setId(-1);
        user.setName("default username");
        user.setMobile("0000");
        return user;
    }
}
