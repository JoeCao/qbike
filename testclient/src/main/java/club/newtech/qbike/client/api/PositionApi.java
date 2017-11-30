package club.newtech.qbike.client.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PositionApi {
    @Autowired
    RestTemplate restTemplate;

    public void positionUpdate(String driverId, String longitude, String latitude) {
        String url = String.format(
                "http://localhost:8050/qbike-trip/trips/updatePosition?driverId=%s&longitude=%s&latitude=%s",
                driverId, longitude, latitude);
        restTemplate.getForObject(
                url, Object.class);
    }
}
