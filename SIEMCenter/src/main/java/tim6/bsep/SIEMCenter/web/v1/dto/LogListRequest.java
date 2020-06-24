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

    private List<Long> ids;

    private List<FacilityType> facilityTypes;

    private List<SeverityLevel> severityLevels;

    private String hostnames;

    private String message;

    private List<LogType> types;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date toDate;

    @Override
    public String toString() {
        return "LogListRequest{" +
                "ids=" + ids +
                ", facilityTypes=" + facilityTypes +
                ", severityLevels=" + severityLevels +
                ", hostnames='" + hostnames + '\'' +
                ", message='" + message + '\'' +
                ", types=" + types +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                '}';
    }
}
