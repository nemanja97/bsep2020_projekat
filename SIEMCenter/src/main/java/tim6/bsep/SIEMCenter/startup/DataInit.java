package tim6.bsep.SIEMCenter.startup;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import tim6.bsep.SIEMCenter.model.Whitelist;
import tim6.bsep.SIEMCenter.service.BlacklistService;
import tim6.bsep.SIEMCenter.service.WhitelistService;

@Component
public class DataInit implements ApplicationRunner {

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    private WhitelistService whitelistService;

    @Override
    public void run(ApplicationArguments args) {
//        Whitelist whitelist = new Whitelist(0, "APPLICATIONS",
//                Lists.newArrayList(
//                        "defenseFirstApp1",
//                        "defenseFirstApp2",
//                        "defenseFirstApp3"
//                ));
//        whitelistService.create(whitelist);
    }
}
