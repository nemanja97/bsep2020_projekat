package tim6.bsep.SIEMCenter.service.implementation;

import org.apache.tools.ant.filters.StringInputStream;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tim6.bsep.SIEMCenter.exceptions.RuleNotCompilingException;
import tim6.bsep.SIEMCenter.model.Rule;
import tim6.bsep.SIEMCenter.repository.RuleRepository;
import tim6.bsep.SIEMCenter.service.RuleService;

import java.util.List;

@Service
public class RuleServiceImpl implements RuleService {

    @Autowired
    RuleRepository ruleRepository;

    @Autowired
    NextSequenceService nextSequenceService;

    private KieSession kieSession;

    @Override
    public List<Rule> getAll() {
        return ruleRepository.findAll();
    }

    @Override
    public Rule findById(Long id) {
        return ruleRepository.findById(id).orElse(null);
    }

    @Override
    public void create(Rule rule) throws RuleNotCompilingException {
        checkIfRuleCompiles(rule);
        rule.setId(nextSequenceService.ruleGetNextSequence());
        ruleRepository.save(rule);
        refreshSession();
    }

    @Override
    public boolean update(Long id, Rule rule) throws RuleNotCompilingException {
        Rule oldRule = findById(id);
        checkIfRuleCompiles(rule);
        if (oldRule != null) {
            oldRule.setConsumes(rule.getConsumes());
            oldRule.setProduces(rule.getProduces());
            oldRule.setContent(rule.getContent());
            oldRule.setName(rule.getName());
            ruleRepository.save(rule);
            refreshSession();
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        Rule rule = findById(id);
        if (rule != null) {
            ruleRepository.delete(rule);
            refreshSession();
            return true;
        }
        return false;
    }

    @Override
    public KieSession getSession() {
        if (kieSession == null)
            refreshSession();
        return kieSession;
    }

    private void checkIfRuleCompiles(Rule rule) throws RuleNotCompilingException {
        KieHelper kieHelper = new KieHelper();
        kieHelper.addResource(ResourceFactory.newInputStreamResource(new StringInputStream(rule.getContent())), ResourceType.DRL);

        KieBaseConfiguration kieBaseConfiguration = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        kieBaseConfiguration.setOption(EventProcessingOption.STREAM);
        kieBaseConfiguration.setOption(EqualityBehaviorOption.EQUALITY);
        kieHelper.build(kieBaseConfiguration);

        Results results = kieHelper.verify();
        if (results.hasMessages(Message.Level.ERROR)) {
            throw new RuleNotCompilingException(results.getMessages(Message.Level.ERROR));
        }
    }

    private void refreshSession() {
        KieHelper kieHelper = new KieHelper();

        List<Rule> rules = getAll();
        rules.forEach(rule -> kieHelper.addResource(
                ResourceFactory.newInputStreamResource(new StringInputStream(rule.getContent())), ResourceType.DRL));

        KieBaseConfiguration kieBaseConfiguration = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        kieBaseConfiguration.setOption(EventProcessingOption.STREAM);
        kieBaseConfiguration.setOption(EqualityBehaviorOption.EQUALITY);
        KieBase kieBase = kieHelper.build(kieBaseConfiguration);
        kieSession = kieBase.newKieSession();
    }
}
