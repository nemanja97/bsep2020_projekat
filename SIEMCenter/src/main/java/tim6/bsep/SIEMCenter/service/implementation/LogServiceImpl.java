package tim6.bsep.SIEMCenter.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tim6.bsep.SIEMCenter.model.Log;
import tim6.bsep.SIEMCenter.model.LogSequences;
import tim6.bsep.SIEMCenter.repository.LogsRepository;
import tim6.bsep.SIEMCenter.service.LogService;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogsRepository logsRepository;

    @Autowired
    private NextSequenceService nextSequenceService;
//
//    public LogServiceImpl(LogsRepository logsRepository) {
//        this.logsRepository = logsRepository;
//    }

    @Override
    public void save(Log log) {
        log.setId(nextSequenceService.getNextSequence("logSequences"));
        logsRepository.save(log);
    }

    @Override
    public Log findById(Long id) {
        return logsRepository.findById(id).orElse(null);
    }
}
