package club.newtech.qbike.intention;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 曹祖鹏 OF506
 * company qianmi.com
 * Date    2018-06-22
 */
@Configuration
public class RabbitConfig {
    @Bean
    public Queue intentionQueue() {
        return new Queue("intention");
    }


}

