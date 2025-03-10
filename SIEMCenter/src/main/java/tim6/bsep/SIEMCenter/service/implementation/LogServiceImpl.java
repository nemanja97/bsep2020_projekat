package tim6.bsep.SIEMCenter.service.implementation;

import com.querydsl.core.types.Predicate;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Global;
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
import tim6.bsep.SIEMCenter.service.*;
import tim6.bsep.SIEMCenter.web.v1.dto.ReportData;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    RuleService ruleService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    public Page<Log> findPredicate(Predicate predicate, Pageable pageable) {
        return logsRepository.findAll(predicate, pageable);
    }

    @Override
    public void save(Log log) {
        KieSession kieSession = ruleService.getSession();
        insertPreviousObjectsAndGlobals();

        log.setId(nextSequenceService.logGetNextSequence());
        logsRepository.save(log);
        kieSession.insert(new LogWrapper(log));
        kieSession.fireAllRules();
        simpMessagingTemplate.convertAndSend("/logs", log);
    }

    private void insertPreviousObjectsAndGlobals() {
        KieSession kieSession = ruleService.getSession();
        if (kieSession.getFactCount() > 0) {
            List<Alarm> alarms = alarmService.findAll();
            List<Log> logs = logsRepository.findAll();

            alarms.forEach(kieSession::insert);
            logs.forEach(previousLog -> kieSession.insert(new LogWrapper(previousLog, true)));
        }
        insertGlobalsIfNeeded(kieSession);
    }

    private void insertGlobalsIfNeeded(KieSession kieSession) {
        final Collection<KiePackage> kiePackages = kieSession.getKieBase().getKiePackages();
        for (KiePackage kiePackage : kiePackages)
        {
            final Collection<Global> globalVariables = kiePackage.getGlobalVariables();
            List<String> globalNames = globalVariables.stream().map(Global::getName).collect(Collectors.toList());
            if (globalNames.contains("whitelistService"))
                kieSession.setGlobal("whitelistService", whitelistService);
            if (globalNames.contains("blacklistService"))
                kieSession.setGlobal("blacklistService", blacklistService);
        }
    }

    @Override
    public Log findById(Long id) {
        return logsRepository.findById(id).orElse(null);
    }


    public List<ReportData> getLogsMonthlyCount(String startDate, String endDate){
        return logsRepository.findLogsMonthlyCount(startDate, endDate);
    }

    public List<ReportData> getLogsWeeklyCount(String startDate, String endDate){
        return logsRepository.findLogsWeeklyCount(startDate, endDate);
    }

    public List<ReportData> getLogsDailyCount(String startDate, String endDate){
        return logsRepository.findLogsDailyCount(startDate, endDate);
    }

    public List<ReportData> getLogsTotalCountByType(String startDate, String endDate){
        return logsRepository.findLogsTotalCountByType(startDate, endDate);
    }

    public List<ReportData> getLogsTotalCountByHostname(String startDate, String endDate){
        return logsRepository.findLogsTotalCountByHostname(startDate, endDate);
    }
}
