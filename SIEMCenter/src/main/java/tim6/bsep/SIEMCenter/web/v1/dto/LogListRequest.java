package tim6.bsep.SIEMCenter.web.v1.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import tim6.bsep.SIEMCenter.model.enums.FacilityType;
import tim6.bsep.SIEMCenter.model.enums.LogType;
import tim6.bsep.SIEMCenter.model.enums.SeverityLevel;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class LogListRequest {

    private List<Long> id;

    private FacilityType facilityType;

    private SeverityLevel severityLevel;

    private String hostnames;

    private String message;

    private LogType logType;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date toDate;

}
