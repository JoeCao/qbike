package club.newtech.qbike.trip.infrastructure;

import club.newtech.qbike.trip.domain.core.vo.Driver;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserRibbonHystrixApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRibbonHystrixApi.class);
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 使用@HystrixCommand注解指定当该方法发生异常时调用的方法
     *
     * @param id customerId
     * @return 通过id查询到的用户
     */
    @HystrixCommand
    public Driver findById(Integer id) {
        Driver driver = this.restTemplate.getForObject("http://QBIKE-UC/users/" + id, Driver.class);
        driver.setId(id);
        return driver;
    }

    /**
     * hystrix fallback方法
     *
     * @param id customerId
     * @return 默认的用户
     */
    public Driver fallback(Integer id) {
        UserRibbonHystrixApi.LOGGER.info("异常发生，进入fallback方法，接收的参数：customerId = {}", id);
        Driver driver = new Driver();
        driver.setId(-1);
        driver.setUserName("default driver");
        driver.setMobile("0000");
        return driver;
    }
}
