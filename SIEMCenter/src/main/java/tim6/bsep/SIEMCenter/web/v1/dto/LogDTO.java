package tim6.bsep.SIEMCenter.web.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tim6.bsep.SIEMCenter.model.enums.FacilityType;
import tim6.bsep.SIEMCenter.model.enums.LogType;
import tim6.bsep.SIEMCenter.model.enums.SeverityLevel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

}
