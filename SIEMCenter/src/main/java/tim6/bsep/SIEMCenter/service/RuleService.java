package tim6.bsep.SIEMCenter.service;

import com.querydsl.core.types.Predicate;
import org.kie.api.runtime.KieSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tim6.bsep.SIEMCenter.exceptions.RuleNotCompilingException;
import tim6.bsep.SIEMCenter.model.Log;
import tim6.bsep.SIEMCenter.model.Rule;

import java.util.List;

public interface RuleService {

    List<Rule> getAll();

    Rule findById(Long id);

    Page<Rule> findPredicate(Predicate predicate, Pageable pageable);

    void create(Rule rule) throws RuleNotCompilingException;

    boolean update(Long id, Rule rule) throws RuleNotCompilingException;

    boolean delete(Long id);

    KieSession getSession();

    void validate(Rule rule) throws RuleNotCompilingException;
}
