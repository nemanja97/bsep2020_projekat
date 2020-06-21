package tim6.bsep.SIEMCenter;

import org.apache.tools.ant.filters.StringInputStream;
import org.drools.compiler.kie.builder.impl.KieBuilderImpl;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieScanner;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.conf.KieBaseOption;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import tim6.bsep.SIEMCenter.model.Rule;
import tim6.bsep.SIEMCenter.repository.LogsRepository;
import tim6.bsep.SIEMCenter.repository.RuleRepository;

import java.util.List;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@EnableMongoRepositories(basePackageClasses = LogsRepository.class)
public class SiemCenterApplication {

	@Autowired
	RuleRepository ruleRepository;

	public static void main(String[] args) {
		SpringApplication.run(SiemCenterApplication.class, args);
	}

//	@Bean
//	public KieSession kieSession() {
//		KieServices ks = KieServices.Factory.get();
//		KieContainer kContainer = ks.newKieContainer(ks.newReleaseId("bsep.integracija", "drools", "0.0.1-SNAPSHOT"));
//		KieScanner kScanner = ks.newKieScanner(kContainer);
//		kScanner.start(10_000);
//		return kContainer.newKieSession("BSEPTim6");
//	}

}
