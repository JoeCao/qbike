package club.newtech.qbike.client.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PositionApi {
    @Autowired
    RestTemplate restTemplate;

    public void positionUpdate() {

    }
}
