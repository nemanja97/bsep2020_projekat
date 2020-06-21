package tim6.bsep.SIEMAgent.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.util.ArrayList;

public class AgentConfig {
    private ArrayList<AgentConfigItem> sources = new ArrayList<>();

    public AgentConfig() {
    }

    public AgentConfig( @JsonProperty(value = "sources", required = true) ArrayList<AgentConfigItem> sources) {
        this.sources = sources;
    }

    public ArrayList<AgentConfigItem> getSources() {
        return sources;
    }

    public void setSources(ArrayList<AgentConfigItem> sources) {
        this.sources = sources;
    }
}
