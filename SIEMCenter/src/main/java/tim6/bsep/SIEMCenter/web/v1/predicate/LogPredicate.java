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
        if (ids != null && !ids.isEmpty()) {
            BooleanBuilder _builder = new BooleanBuilder();
            ids.forEach(alarmId -> _builder.or(qLog.id.eq(alarmId)));
            builder.and(_builder);
        }

        List<FacilityType> facilityTypes = request.getFacilityTypes();
        if (facilityTypes != null && !facilityTypes.isEmpty()) {
            BooleanBuilder _builder = new BooleanBuilder();
            facilityTypes.forEach(facilityType -> _builder.or(qLog.facilityType.eq(facilityType)));
            builder.and(_builder);
        }

        List<SeverityLevel> severityLevels = request.getSeverityLevels();
        if (severityLevels != null && !severityLevels.isEmpty()) {
            BooleanBuilder _builder = new BooleanBuilder();
            severityLevels.forEach(severityLevel -> _builder.or(qLog.severityLevel.eq(severityLevel)));
            builder.and(_builder);
        }

        String hostname = request.getHostnames();
        if (hostname != null)
            builder.and(qLog.hostname.matches(hostname));

        String message = request.getMessage();
        if (message != null && !message.isBlank())
            builder.and(qLog.message.matches(message));

        List<LogType> logTypes = request.getTypes();
        if (logTypes != null && !logTypes.isEmpty()) {
            BooleanBuilder _builder = new BooleanBuilder();
            logTypes.forEach(logType -> _builder.or(qLog.type.eq(logType)));
            builder.and(_builder);
        }

        Date fromDate = request.getFromDate();
        if (fromDate != null)
            builder.and(qLog.timestamp.after(fromDate));

        Date toDate = request.getToDate();
        if (toDate != null)
            builder.and(qLog.timestamp.before(toDate));

        return builder;
    }
}
