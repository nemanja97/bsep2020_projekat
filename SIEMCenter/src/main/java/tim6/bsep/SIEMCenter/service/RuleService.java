package tim6.bsep.SIEMCenter.service;

import org.kie.api.runtime.KieSession;
import tim6.bsep.SIEMCenter.exceptions.RuleNotCompilingException;
import tim6.bsep.SIEMCenter.model.Rule;

import java.util.List;

public interface RuleService {

    List<Rule> getAll();

    Rule findById(Long id);

    void create(Rule rule) throws RuleNotCompilingException;

    boolean update(Long id, Rule rule) throws RuleNotCompilingException;

    boolean delete(Long id);

    KieSession getSession();
}
