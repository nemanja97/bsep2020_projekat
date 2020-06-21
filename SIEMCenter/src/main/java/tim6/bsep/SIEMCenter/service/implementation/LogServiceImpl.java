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
import tim6.bsep.SIEMCenter.service.LogService;

import java.util.Collection;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogsRepository logsRepository;

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private NextSequenceService nextSequenceService;

    @Autowired
    private KieSession kieSession;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    public Page<Log> findPredicate(Predicate predicate, Pageable pageable) {
        return logsRepository.findAll(predicate, pageable);
    }

    @Override
    public void save(Log log) {
        log.setId(nextSequenceService.getNextSequence("logSequences"));
        logsRepository.save(log);
        kieSession.insert(new LogWrapper(log));
        kieSession.fireAllRules();
        simpMessagingTemplate.convertAndSend("/logs", log);
    }

    @Override
    public Log findById(Long id) {
        return logsRepository.findById(id).orElse(null);
    }
}
