package tim6.bsep.SIEMCenter.service.implementation;

import com.querydsl.core.types.Predicate;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tim6.bsep.SIEMCenter.model.Log;
import tim6.bsep.SIEMCenter.model.drools.Alarm;
import tim6.bsep.SIEMCenter.model.drools.LogWrapper;
import tim6.bsep.SIEMCenter.repository.LogsRepository;
import tim6.bsep.SIEMCenter.service.AlarmService;
import tim6.bsep.SIEMCenter.service.BlacklistService;
import tim6.bsep.SIEMCenter.service.LogService;
import tim6.bsep.SIEMCenter.service.WhitelistService;

import java.util.Collection;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogsRepository logsRepository;

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private NextSequenceService nextSequenceService;

    @Autowired
    BlacklistService blacklistService;

    @Autowired
    WhitelistService whitelistService;

    @Autowired
    private KieSession kieSession;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    public Page<Log> findPredicate(Predicate predicate, Pageable pageable) {
        return logsRepository.findAll(predicate, pageable);
    }

    @Override
    public void save(Log log) {
        insertPreviousObjectsAndGlobals();

        log.setId(nextSequenceService.logGetNextSequence());
        logsRepository.save(log);
        kieSession.insert(new LogWrapper(log));
        kieSession.fireAllRules();
    }

    private void insertPreviousObjectsAndGlobals() {
        if (kieSession.getFactCount() > 0) {
            List<Alarm> alarms = alarmService.findAll();
            List<Log> logs = logsRepository.findAll();

            alarms.forEach(previousAlarm -> kieSession.insert(previousAlarm));
            logs.forEach(previousLog -> kieSession.insert(new LogWrapper(previousLog, true)));
        }
        kieSession.setGlobal("whitelistService", whitelistService);
        kieSession.setGlobal("blacklistService", blacklistService);
    }

    @Override
    public Log findById(Long id) {
        return logsRepository.findById(id).orElse(null);
    }
}
