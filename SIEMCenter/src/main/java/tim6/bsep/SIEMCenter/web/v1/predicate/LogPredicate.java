package tim6.bsep.SIEMCenter.web.v1.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import tim6.bsep.SIEMCenter.model.QLog;
import tim6.bsep.SIEMCenter.model.enums.FacilityType;
import tim6.bsep.SIEMCenter.model.enums.LogType;
import tim6.bsep.SIEMCenter.model.enums.SeverityLevel;
import tim6.bsep.SIEMCenter.web.v1.dto.LogListRequest;

import java.util.Date;
import java.util.List;

public class LogPredicate {

    public Predicate makeQuery(LogListRequest request){
        QLog qLog = new QLog("log");
        BooleanBuilder builder = new BooleanBuilder();

        List<Long> ids = request.getIds();
        if (ids != null && !ids.isEmpty())
            ids.forEach(id -> builder.or(qLog.id.eq(id)));

        List<FacilityType> facilityTypes = request.getFacilityTypes();
        if (facilityTypes != null && !facilityTypes.isEmpty())
            facilityTypes.forEach(facilityType -> builder.or(qLog.facilityType.eq(facilityType)));

        List<SeverityLevel> severityLevels = request.getSeverityLevels();
        if (severityLevels != null && !severityLevels.isEmpty())
            severityLevels.forEach(severityLevel -> builder.or(qLog.severityLevel.eq(severityLevel)));

        String hostname = request.getHostnames();
        if (hostname != null)
            builder.and(qLog.hostname.containsIgnoreCase(hostname));

        String message = request.getMessage();
        if (message != null && !message.isBlank())
            builder.and(qLog.message.containsIgnoreCase(message));

        List<LogType> logTypes = request.getTypes();
        if (logTypes != null && !logTypes.isEmpty())
            logTypes.forEach(logType ->  builder.or(qLog.type.eq(logType)));

        Date fromDate = request.getFromDate();
        if (fromDate != null)
            builder.and(qLog.timestamp.after(fromDate));

        Date toDate = request.getToDate();
        if (toDate != null)
            builder.and(qLog.timestamp.before(toDate));

        return builder;
    }
}
