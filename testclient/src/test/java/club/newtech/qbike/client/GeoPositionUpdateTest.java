package club.newtech.qbike.client;

import club.newtech.qbike.client.api.PositionApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GeoPositionUpdateTest {
    @Value("classpath:/idposition.csv")
    private org.springframework.core.io.Resource resource;
    @Autowired
    private PositionApi positionApi;

    @Test
    public void positionUpdateClient() {
        try {
            Files.lines(resource.getFile().toPath()).forEach(line -> {
                try {
                    String[] values = line.split(",");
                    positionApi.positionUpdate(values[0], values[1], values[2]);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
