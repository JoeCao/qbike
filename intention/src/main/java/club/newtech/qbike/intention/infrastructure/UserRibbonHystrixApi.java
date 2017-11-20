package club.newtech.qbike.intention.infrastructure;

import club.newtech.qbike.intention.domain.core.vo.Customer;
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
     * @param id id
     * @return 通过id查询到的用户
     */
    @HystrixCommand(fallbackMethod = "fallback")
    public Customer findById(Integer id) {
        return this.restTemplate.getForObject("http://QBIKE-UC/users/" + id, Customer.class);
    }

    /**
     * hystrix fallback方法
     *
     * @param id id
     * @return 默认的用户
     */
    public Customer fallback(Integer id) {
        UserRibbonHystrixApi.LOGGER.info("异常发生，进入fallback方法，接收的参数：id = {}", id);
        Customer customer = new Customer();
        customer.setId(-1);
        customer.setName("default username");
        customer.setMobile("0000");
        return customer;
    }
}
