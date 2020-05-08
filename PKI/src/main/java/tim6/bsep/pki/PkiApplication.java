package tim6.bsep.pki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PkiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PkiApplication.class, args);
    }

}
