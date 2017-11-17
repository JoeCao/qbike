package club.newtech.qbike.uc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("club.newtech.qbike.uc.domain.root")
public class UcApplication {

	public static void main(String[] args) {
		SpringApplication.run(UcApplication.class, args);
	}
}
