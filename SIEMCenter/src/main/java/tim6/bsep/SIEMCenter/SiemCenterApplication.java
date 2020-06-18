package tim6.bsep.SIEMCenter;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import tim6.bsep.SIEMCenter.repository.LogsRepository;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@EnableMongoRepositories(basePackageClasses = LogsRepository.class)
public class SiemCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiemCenterApplication.class, args);
	}

	@Bean
	public KieSession kieSession() {
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.newKieContainer(ks.newReleaseId("bsep.integracija", "drools", "0.0.1-SNAPSHOT"));
		KieScanner kScanner = ks.newKieScanner(kContainer);
		kScanner.start(10_000);
		return kContainer.newKieSession("BSEPTim6");
	}

}
