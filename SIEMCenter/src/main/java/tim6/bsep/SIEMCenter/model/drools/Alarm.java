package tim6.bsep.SIEMCenter.model.drools;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;
import tim6.bsep.SIEMCenter.model.Log;
import tim6.bsep.SIEMCenter.model.enums.FacilityType;
import tim6.bsep.SIEMCenter.model.enums.LogType;
import tim6.bsep.SIEMCenter.model.enums.SeverityLevel;

import java.util.*;

@Setter
@Getter
@AllArgsConstructor
@Role(Role.Type.EVENT)
@Timestamp("timestamp")
public class Alarm {

    private long id;

    private Set<Long> logIds = new HashSet<>();

    private Date timestamp = new Date();

    private FacilityType facilityType;

    private SeverityLevel severityLevel;

    private Set<String> hostnames = new HashSet<>();

    private String message;

    private LogType type;

    public Alarm(){
    }

    public Alarm(Log log){
        this.logIds.add(log.getId());
        this.facilityType = log.getFacilityType();
        this.hostnames.add(log.getHostname());
        this.type = log.getType();
    }

    public Alarm(Log log, SeverityLevel severityLevel, String message){
        this.logIds.add(log.getId());
        this.facilityType = log.getFacilityType();
        this.hostnames.add(log.getHostname());
        this.type = log.getType();
        this.severityLevel = severityLevel;
        this.message = message;
    }
}
