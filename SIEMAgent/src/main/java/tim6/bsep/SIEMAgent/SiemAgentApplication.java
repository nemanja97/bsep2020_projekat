package tim6.bsep.SIEMAgent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties
public class SiemAgentApplication {

	public static void main(String[] args) {SpringApplication.run(SiemAgentApplication.class, args);}
}
