package club.newtech.qbick.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class ZuulGateway {
    public static void main(String[] args) {
        SpringApplication.run(ZuulGateway.class, args);
    }
}
