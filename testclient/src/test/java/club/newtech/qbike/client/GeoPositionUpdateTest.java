package club.newtech.qbike.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GeoPositionUpdateTest {
    @Value("classpath:/idposition.csv")
    private org.springframework.core.io.Resource resource;
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void positionUpdateClient() {
        try {
            Files.lines(resource.getFile().toPath()).forEach(line -> {
                String[] values = line.split(",");
                Map<String, String> requests = new HashMap();
                requests.put("driverId", values[0]);
                requests.put("longitude", values[1]);
                requests.put("latitude", values[2]);
                String url = String.format(
                        "http://qbike-trip/trips/updatePosition?driverId=%s&longitude=%s&latitude=%s",
                        values[0], values[1], values[2]);
                restTemplate.getForObject(
                        url, Object.class);

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
