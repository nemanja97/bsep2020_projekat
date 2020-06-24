package tim6.bsep.SIEMCenter.startup;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import tim6.bsep.SIEMCenter.model.Rule;
import tim6.bsep.SIEMCenter.model.Whitelist;
import tim6.bsep.SIEMCenter.model.enums.SeverityLevel;
import tim6.bsep.SIEMCenter.service.BlacklistService;
import tim6.bsep.SIEMCenter.service.RuleService;
import tim6.bsep.SIEMCenter.service.WhitelistService;

@Component
public class DataInit implements ApplicationRunner {

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    private WhitelistService whitelistService;

    @Autowired
    private RuleService ruleService;

    @Override
    public void run(ApplicationArguments args) {
//        Rule rule = new Rule(
//                0L, "Backup failed", "package bsep.integration.predefined;\n" +
//                "import java.util.List;\n" +
//                "import tim6.bsep.SIEMCenter.model.drools.LogWrapper;\n" +
//                "import tim6.bsep.SIEMCenter.model.drools.Alarm\n" +
//                "import tim6.bsep.SIEMCenter.model.enums.SeverityLevel\n" +
//                "import org.apache.commons.lang3.StringUtils;\n\nrule \"Backup failed\"\n" +
//                "salience 2\n" +
//                "    when\n" +
//                "        $logWrapper: LogWrapper(log.getSeverityLevel() == SeverityLevel.ERROR, handled == false,\n" +
//                "            log.getMessage().equals(\"Backup failed\"))\n" +
//                "    then\n" +
//                "        System.out.println(\"Backup failed\");\n" +
//                "        String alarmMessage = \"Backup failed\";\n" +
//                "        Alarm alarm = new Alarm($logWrapper.getLog(), SeverityLevel.ERROR, alarmMessage);\n" +
//                "        $logWrapper.setHandled(true);\n" +
//                "        update($logWrapper);\n" +
//                "        insert(alarm);\n" +
//                "end", SeverityLevel.ERROR, SeverityLevel.ERROR
//        );
//        ruleService.create(rule);
//        Whitelist whitelist = new Whitelist(0, "APPLICATIONS",
//                Lists.newArrayList(
//                        "defenseFirstApp1",
//                        "defenseFirstApp2",
//                        "defenseFirstApp3"
//                ));
//        whitelistService.create(whitelist);
    }
}
