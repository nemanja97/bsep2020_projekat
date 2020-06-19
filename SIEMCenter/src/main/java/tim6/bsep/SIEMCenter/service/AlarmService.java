package tim6.bsep.SIEMCenter.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tim6.bsep.SIEMCenter.model.drools.Alarm;
import tim6.bsep.SIEMCenter.model.enums.FacilityType;
import tim6.bsep.SIEMCenter.model.enums.LogType;
import tim6.bsep.SIEMCenter.model.enums.SeverityLevel;

import java.util.Date;
import java.util.List;

public interface AlarmService {

    void save(Alarm alarm);

    void saveNewAlarmsFromSession();

    Alarm findById(Long id);

    Alarm findNewest();

    Page<Alarm> findPredicate(Predicate predicate, Pageable pageable);

    Predicate makeQuery(List<Long> ids, List<Long> longIds, Date fromDate, Date toDate, List<FacilityType> facilityTypes,
                        List<SeverityLevel> severityLevels, List<String> hostnames, String message, LogType logType);

}
