package tim6.bsep.SIEMCenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import tim6.bsep.SIEMCenter.repository.LogsRepository;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@EnableMongoRepositories(basePackageClasses = LogsRepository.class)
public class SiemCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiemCenterApplication.class, args);
	}

}
