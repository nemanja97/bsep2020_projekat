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

    public AgentConfigItem() {
    }

    public AgentConfigItem(@JsonProperty(value = "path", required = true) String path,
                           @JsonProperty(value = "regexList")ArrayList<String> regexList,
                           @JsonProperty(value = "simulated", required = true)Boolean simulated,
                           @JsonProperty(value = "batch", required = true) Boolean batch,
                           @JsonProperty(value = "batchTime")Integer batchTime) {
        this.path = path;
        this.regexList = regexList;
        this.simulated = simulated;
        this.batch = batch;
        if(batchTime != null){
            this.batchTime = batchTime;
        }else{
            this.batchTime = 0;
        }
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
}
