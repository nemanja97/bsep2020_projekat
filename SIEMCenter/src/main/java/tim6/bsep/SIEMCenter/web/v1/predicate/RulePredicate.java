package tim6.bsep.SIEMCenter.web.v1.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import tim6.bsep.SIEMCenter.model.QRule;
import tim6.bsep.SIEMCenter.model.enums.SeverityLevel;
import tim6.bsep.SIEMCenter.web.v1.dto.RuleListRequest;

import java.util.List;

public class RulePredicate {

    public Predicate makeQuery(RuleListRequest request){
        QRule qRule = new QRule("rule");
        BooleanBuilder builder = new BooleanBuilder();

        List<Long> ids = request.getIds();
        if (ids != null && !ids.isEmpty()) {
            BooleanBuilder _builder = new BooleanBuilder();
            ids.forEach(alarmId -> _builder.or(qRule.id.eq(alarmId)));
            builder.and(_builder);
        }

        List<SeverityLevel> produces = request.getProduces();
        if (produces != null && !produces.isEmpty()) {
            BooleanBuilder _builder = new BooleanBuilder();
            produces.forEach(severityLevel -> _builder.or(qRule.produces.eq(severityLevel)));
            builder.and(_builder);
        }

        List<SeverityLevel> consumes = request.getConsumes();
        if (consumes != null && !consumes.isEmpty()) {
            BooleanBuilder _builder = new BooleanBuilder();
            consumes.forEach(severityLevel -> _builder.or(qRule.consumes.eq(severityLevel)));
            builder.and(_builder);
        }

        String message = request.getName();
        if (message != null && !message.isBlank())
            builder.and(qRule.name.matches(message));

        return builder;
    }
}
