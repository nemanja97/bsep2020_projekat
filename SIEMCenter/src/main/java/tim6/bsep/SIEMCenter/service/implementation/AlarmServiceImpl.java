package tim6.bsep.SIEMCenter.service.implementation;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tim6.bsep.SIEMCenter.model.drools.Alarm;
import tim6.bsep.SIEMCenter.model.drools.QAlarm;
import tim6.bsep.SIEMCenter.model.enums.FacilityType;
import tim6.bsep.SIEMCenter.model.enums.LogType;
import tim6.bsep.SIEMCenter.model.enums.SeverityLevel;
import tim6.bsep.SIEMCenter.web.v1.dto.ReportData;
import tim6.bsep.SIEMCenter.repository.AlarmsRepository;
import tim6.bsep.SIEMCenter.service.AlarmService;
import tim6.bsep.SIEMCenter.service.RuleService;
import tim6.bsep.SIEMCenter.web.v1.dto.AlarmListRequest;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    AlarmsRepository alarmsRepository;

    @Autowired
    NextSequenceService nextSequenceService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    RuleService ruleService;

    @Override
    public List<Alarm> findAll() {
        return alarmsRepository.findAll();
    }

    @Override
    public void save(Alarm alarm) {
        alarm.setId(nextSequenceService.alarmGetNextSequence());
        alarmsRepository.save(alarm);

        simpMessagingTemplate.convertAndSend("/topic/messages", alarm);
    }

    @Override
    public void saveNewAlarmsFromSession() {
        KieSession kieSession = ruleService.getSession();

        Alarm lastSavedAlarm = findNewest();
        Collection<Alarm> sessionAlarms = (Collection<Alarm>) kieSession.getObjects(new ClassObjectFilter(Alarm.class));
        if (sessionAlarms != null && !sessionAlarms.isEmpty()) {
            if(lastSavedAlarm != null){
                sessionAlarms.forEach(alarm -> {
                    if (alarm.getTimestamp().after(lastSavedAlarm.getTimestamp()))
                        save(alarm);
                });
            }else{
                sessionAlarms.forEach(this::save);
            }

        }
    }

    @Override
    public Alarm findById(Long id) {
        return alarmsRepository.findById(id).orElse(null);
    }

    @Override
    public Alarm findNewest() {
        return alarmsRepository.findFirstByOrderByTimestampDesc();
    }

    @Override
    public Page<Alarm> findPredicate(Predicate predicate, Pageable pageable) {
        return alarmsRepository.findAll(predicate, pageable);
    }

    @Override
    public Predicate makeQuery(AlarmListRequest request) {
        QAlarm qAlarm = new QAlarm("alarm");
        BooleanBuilder builder = new BooleanBuilder();

        List<Long> ids = request.getIds();
        if (ids != null && !ids.isEmpty()) {
            BooleanBuilder _builder = new BooleanBuilder();
            ids.forEach(alarmId -> _builder.or(qAlarm.id.eq(alarmId)));
            builder.and(_builder);
        }

        List<FacilityType> facilityTypes = request.getFacilityTypes();
        if (facilityTypes != null && !facilityTypes.isEmpty()) {
            BooleanBuilder _builder = new BooleanBuilder();
            facilityTypes.forEach(facilityType -> _builder.or(qAlarm.facilityType.eq(facilityType)));
            builder.and(_builder);
        }

        List<SeverityLevel> severityLevels = request.getSeverityLevels();
        if (severityLevels != null && !severityLevels.isEmpty()) {
            BooleanBuilder _builder = new BooleanBuilder();
            severityLevels.forEach(severityLevel -> _builder.or(qAlarm.severityLevel.eq(severityLevel)));
            builder.and(_builder);
        }

        String hostname = request.getHostnames();
        if (hostname != null)
            builder.and(qAlarm.hostnames.contains(hostname));

        String message = request.getMessage();
        if (message != null && !message.isBlank())
            builder.and(qAlarm.message.matches(message));

        List<LogType> logTypes = request.getTypes();
        if (logTypes != null && !logTypes.isEmpty()) {
            BooleanBuilder _builder = new BooleanBuilder();
            logTypes.forEach(logType -> _builder.or(qAlarm.type.eq(logType)));
            builder.and(_builder);
        }

        Date fromDate = request.getFromDate();
        if (fromDate != null)
            builder.and(qAlarm.timestamp.after(fromDate));

        Date toDate = request.getToDate();
        if (toDate != null)
            builder.and(qAlarm.timestamp.before(toDate));

        return builder;
    }

    public List<ReportData> getAlarmsMonthlyCount(String startDate, String endDate){
        return alarmsRepository.findAlarmsMonthlyCount(startDate, endDate);
    }

    public List<ReportData> getAlarmsWeeklyCount(String startDate, String endDate){
        return alarmsRepository.findAlarmsWeeklyCount(startDate, endDate);
    }

    public List<ReportData> getAlarmsDailyCount(String startDate, String endDate){
        return alarmsRepository.findAlarmsDailyCount(startDate, endDate);
    }

    public List<ReportData> getAlarmsTotalCountByType(String startDate, String endDate){
        return alarmsRepository.findAlarmsTotalCountByType(startDate, endDate);
    }

    public List<ReportData> getAlarmsTotalCountByHostname(String startDate, String endDate){
        return alarmsRepository.findAlarmsTotalCountByHostname(startDate, endDate);
    }


}
