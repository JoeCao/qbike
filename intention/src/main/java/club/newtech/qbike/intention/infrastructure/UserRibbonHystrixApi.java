package club.newtech.qbike.intention.infrastructure;

import club.newtech.qbike.intention.domain.core.vo.Customer;
import club.newtech.qbike.intention.domain.core.vo.DriverVo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

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
    public Customer findById(Integer id) {
//        return this.restTemplate.getForObject("http://QBIKE-UC/users/" + customerId, Customer.class);
        Map ret = restTemplate.getForObject("http://QBIKE-UC/users/" + id, Map.class);
        Customer customerVo = new Customer();
        customerVo.setCustomerId(id);
        customerVo.setCustomerMobile(String.valueOf(ret.get("mobile")));
        customerVo.setCustomerName(String.valueOf(ret.get("userName")));
        customerVo.setUserType(String.valueOf(ret.get("type")));
        return customerVo;
    }

    @HystrixCommand
    public DriverVo findDriverById(Integer id) {
        Map ret = restTemplate.getForObject("http://QBIKE-UC/users/" + id, Map.class);
        DriverVo driverVo = new DriverVo();
        driverVo.setId(id);
        driverVo.setMobile(String.valueOf(ret.get("mobile")));
        driverVo.setUserName(String.valueOf(ret.get("userName")));
        driverVo.setType(String.valueOf(ret.get("type")));
        return driverVo;
    }

    /**
     * hystrix fallback方法
     *
     * @param id customerId
     * @return 默认的用户
     */
    public Customer fallback(Integer id) {
        UserRibbonHystrixApi.LOGGER.info("异常发生，进入fallback方法，接收的参数：customerId = {}", id);
        Customer customer = new Customer();
        customer.setCustomerId(-1);
        customer.setCustomerName("default username");
        customer.setCustomerMobile("0000");
        return customer;
    }
}
