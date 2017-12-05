package club.newtech.qbike.order.infrastructure;

import club.newtech.qbike.order.domain.core.vo.CustomerVo;
import club.newtech.qbike.order.domain.core.vo.DriverVo;
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
    @HystrixCommand()
    public CustomerVo findCustomerById(Integer id) {
        Map ret = restTemplate.getForObject("http://QBIKE-UC/users/" + id, Map.class);
        CustomerVo customerVo = new CustomerVo();
        customerVo.setCustomerId(id);
        customerVo.setCustomerMobile(String.valueOf(ret.get("mobile")));
        customerVo.setCustomerName(String.valueOf(ret.get("userName")));
        return customerVo;
    }

    @HystrixCommand()
    public DriverVo findDriverById(Integer id) {
        Map ret = restTemplate.getForObject("http://QBIKE-UC/users/" + id, Map.class);
        DriverVo driverVo = new DriverVo();
        driverVo.setDriverId(id);
        driverVo.setDriverMobile(String.valueOf(ret.get("mobile")));
        driverVo.setDriverName(String.valueOf(ret.get("userName")));
        return driverVo;
    }

    /**
     * hystrix fallback方法
     *
     * @param id customerId
     * @return 默认的用户
     */
    public CustomerVo fallback(Integer id) {
        LOGGER.info("异常发生，进入fallback方法，接收的参数：customerId = {}", id);
        CustomerVo customer = new CustomerVo();
        customer.setCustomerId(-1);
        customer.setCustomerName("default username");
        customer.setCustomerMobile("0000");
        return customer;
    }

    public DriverVo fallbackDriver(Integer id) {
        LOGGER.info("异常发生，进入fallback方法，接收的参数：customerId = {}", id);
        DriverVo driverVo = new DriverVo();
        driverVo.setDriverId(-1);
        driverVo.setDriverName("default");
        driverVo.setDriverMobile("0000");
        return driverVo;

    }
}
