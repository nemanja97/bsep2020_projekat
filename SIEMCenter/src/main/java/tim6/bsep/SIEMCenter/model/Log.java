package tim6.bsep.SIEMCenter.model;

import com.querydsl.core.annotations.QueryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import tim6.bsep.SIEMCenter.model.enums.FacilityType;
import tim6.bsep.SIEMCenter.model.enums.LogType;
import tim6.bsep.SIEMCenter.model.enums.SeverityLevel;

import java.util.Date;

@Document(collection = "logs")
@QueryEntity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Log {

    private long id;

    private Date timestamp;

    private FacilityType facilityType;

    private SeverityLevel severityLevel;

    private String hostname;

    private String message;

    private LogType type;
}
