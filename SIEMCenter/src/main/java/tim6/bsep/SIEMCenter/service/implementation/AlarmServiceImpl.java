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
import tim6.bsep.SIEMCenter.repository.AlarmsRepository;
import tim6.bsep.SIEMCenter.service.AlarmService;
import tim6.bsep.SIEMCenter.service.RuleService;

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
        sessionAlarms.forEach(alarm -> {
            if (alarm.getTimestamp().after(lastSavedAlarm.getTimestamp()))
                save(alarm);
        });
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
    public Predicate makeQuery(List<Long> ids, List<Long> longIds, Date fromDate, Date toDate, List<FacilityType> facilityTypes,
                               List<SeverityLevel> severityLevels, List<String> hostnames, String message, LogType logType) {
        QAlarm qAlarm = new QAlarm("alarm");
        BooleanBuilder builder = new BooleanBuilder();

        if (ids != null && !ids.isEmpty()) {
            BooleanBuilder _builder = new BooleanBuilder();
            ids.forEach(alarmId -> _builder.or(qAlarm.id.eq(alarmId)));
            builder.and(_builder);
        }

        if (longIds != null && !longIds.isEmpty()) {
            BooleanBuilder _builder = new BooleanBuilder();
            longIds.forEach(logId -> _builder.or(qAlarm.logIds.contains(logId)));
            builder.and(_builder);
        }

        if (fromDate != null) {
            builder.and(qAlarm.timestamp.after(fromDate));
        }

        if (toDate != null)
            builder.and(qAlarm.timestamp.before(toDate));

        if (facilityTypes != null && !facilityTypes.isEmpty()) {
            BooleanBuilder _builder = new BooleanBuilder();
            facilityTypes.forEach(facilityType -> _builder.or(qAlarm.facilityType.eq(facilityType)));
            builder.and(_builder);
        }

        if (severityLevels != null && !severityLevels.isEmpty()) {
            BooleanBuilder _builder = new BooleanBuilder();
            severityLevels.forEach(severityLevel -> _builder.or(qAlarm.severityLevel.eq(severityLevel)));
            builder.and(_builder);
        }

        if (hostnames != null && !hostnames.isEmpty()) {
            BooleanBuilder _builder = new BooleanBuilder();
            hostnames.forEach(hostname -> _builder.or(qAlarm.hostnames.contains(hostname)));
            builder.and(_builder);
        }

        if (message != null && !message.isBlank())
            builder.and(qAlarm.message.matches(message));

        if (logType != null)
            builder.and(qAlarm.type.eq(logType));

        return builder;
    }


}
