package tim6.bsep.SIEMCenter.web.v1.dto;

import lombok.Getter;
import lombok.Setter;
import tim6.bsep.SIEMCenter.model.enums.LogType;

@Getter
@Setter
public class ReportData {

    private String date;

    private LogType type;

    private String hostname;

    private int count;

    private int sysCount;

    private int simCount;

}
