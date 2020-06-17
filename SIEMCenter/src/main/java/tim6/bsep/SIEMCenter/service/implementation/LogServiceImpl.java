package tim6.bsep.SIEMCenter.service.implementation;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tim6.bsep.SIEMCenter.model.Log;
import tim6.bsep.SIEMCenter.model.drools.LogWrapper;
import tim6.bsep.SIEMCenter.repository.LogsRepository;
import tim6.bsep.SIEMCenter.service.LogService;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogsRepository logsRepository;

    @Autowired
    private NextSequenceService nextSequenceService;

    @Autowired
    private KieSession kieSession;
//
//    public LogServiceImpl(LogsRepository logsRepository) {
//        this.logsRepository = logsRepository;
//    }

    @Override
    public void save(Log log) {
        log.setId(nextSequenceService.getNextSequence("logSequences"));
        logsRepository.save(log);
        kieSession.insert(new LogWrapper(log));
        kieSession.fireAllRules();
    }

    @Override
    public Log findById(Long id) {
        return logsRepository.findById(id).orElse(null);
    }
}
