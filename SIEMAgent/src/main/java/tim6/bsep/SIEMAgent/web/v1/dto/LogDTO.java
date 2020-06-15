package tim6.bsep.SIEMAgent.web.v1.dto;

import lombok.Getter;
import lombok.Setter;
import tim6.bsep.SIEMAgent.model.enums.FacilityType;
import tim6.bsep.SIEMAgent.model.enums.LogType;
import tim6.bsep.SIEMAgent.model.enums.SeverityLevel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class LogDTO {

    private long id;

    @NotNull
    private Date timestamp;

    @NotNull
    private FacilityType facilityType;

    @NotNull
    private SeverityLevel severityLevel;

    @NotBlank
    private String hostname;

    @NotBlank
    private String message;

    @NotNull
    private LogType type;

    public LogDTO(@NotNull Date timestamp, @NotNull FacilityType facilityType, @NotNull SeverityLevel severityLevel,
                  @NotBlank String hostname, @NotBlank String message, @NotNull LogType type) {
        this.timestamp = timestamp;
        this.facilityType = facilityType;
        this.severityLevel = severityLevel;
        this.hostname = hostname;
        this.message = message;
        this.type = type;
    }
}
