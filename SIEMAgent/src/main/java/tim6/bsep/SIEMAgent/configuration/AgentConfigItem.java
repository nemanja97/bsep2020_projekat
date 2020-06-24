package tim6.bsep.SIEMAgent.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class AgentConfigItem {
    private String path;
    private ArrayList<String> regexList = new ArrayList<>();
    private Boolean simulated;
    private Boolean batch;
    private Integer batchTime;
    private Boolean windows;
    private String pattern;
    private ArrayList<String> groups = new ArrayList<>();

    public AgentConfigItem() {
    }

    public AgentConfigItem(@JsonProperty(value = "path", required = true) String path,
                           @JsonProperty(value = "regexList")ArrayList<String> regexList,
                           @JsonProperty(value = "simulated", required = true)Boolean simulated,
                           @JsonProperty(value = "batch", required = true) Boolean batch,
                           @JsonProperty(value = "batchTime")Integer batchTime,
                           @JsonProperty(value = "windows") Boolean windows,
                           @JsonProperty(value = "pattern")String pattern,
                           @JsonProperty(value = "groups")ArrayList<String> groups) {
        this.path = path;
        this.regexList = regexList;
        this.simulated = simulated;
        this.batch = batch;
        if(batchTime != null){
            this.batchTime = batchTime;
        }else{
            this.batchTime = 0;
        }
        this.windows = windows;
        this.pattern = pattern;
        this.groups = groups;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<String> getRegexList() {
        return regexList;
    }

    public void setRegexList(ArrayList<String> regexList) {
        this.regexList = regexList;
    }

    public Boolean getSimulated() {
        return simulated;
    }

    public void setSimulated(Boolean simulated) {
        this.simulated = simulated;
    }

    public Boolean getBatch() {
        return batch;
    }

    public void setBatch(Boolean batch) {
        this.batch = batch;
    }

    public Integer getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(Integer batchTime) {
        this.batchTime = batchTime;
    }

    public Boolean getWindows() {
        return windows;
    }

    public void setWindows(Boolean windows) {
        this.windows = windows;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }
}
