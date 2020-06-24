package tim6.bsep.SIEMAgent.startup;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tim6.bsep.SIEMAgent.configuration.AgentConfig;
import tim6.bsep.SIEMAgent.configuration.AgentConfigItem;
import tim6.bsep.SIEMAgent.service.WatcherService;
import tim6.bsep.SIEMAgent.utility.SignatureUtility;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static tim6.bsep.SIEMAgent.utility.ACLUtility.setupACL;

@Component
public class Startup implements ApplicationRunner {

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private WatcherService watcherService;

    @Override
    public void run(ApplicationArguments args) throws IOException {
        setupACL();
        for (AgentConfigItem item : readConfig().getSources()) {
            File f = new File(item.getPath());
            if(!item.getWindows()){
                taskExecutor.execute(watcherService.watch(item));
            }else{
                taskExecutor.execute(watcherService.watchWindows(item));
            }
        }
    }

    private AgentConfig readConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File configuration = new File("./configuration.json");
        return mapper.readValue(configuration, AgentConfig.class);
    }

}

