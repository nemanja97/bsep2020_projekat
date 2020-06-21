package tim6.bsep.SIEMCenter.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tim6.bsep.SIEMCenter.model.drools.Alarm;
import tim6.bsep.SIEMCenter.web.v1.dto.AlarmListRequest;

import java.util.List;

public interface AlarmService {

    List<Alarm> findAll();

    void save(Alarm alarm);

    void saveNewAlarmsFromSession();

    Alarm findById(Long id);

    Alarm findNewest();

    Page<Alarm> findPredicate(Predicate predicate, Pageable pageable);

    Predicate makeQuery(AlarmListRequest request);

}
