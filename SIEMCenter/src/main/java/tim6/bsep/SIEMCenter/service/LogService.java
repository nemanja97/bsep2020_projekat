package tim6.bsep.SIEMCenter.service;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tim6.bsep.SIEMCenter.model.Log;

public interface LogService {

    Page<Log> findPredicate(Predicate predicate, Pageable pageable);

    Log findById(Long id);

    void save(Log log);
}
