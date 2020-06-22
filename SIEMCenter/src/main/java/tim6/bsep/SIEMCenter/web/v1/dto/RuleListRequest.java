package tim6.bsep.SIEMCenter.web.v1.dto;

import lombok.Getter;
import lombok.Setter;
import tim6.bsep.SIEMCenter.model.enums.SeverityLevel;

import java.util.List;

@Getter
@Setter
public class RuleListRequest {

    private List<Long> ids;

    private String name;

    private List<SeverityLevel> produces;

    private List<SeverityLevel> consumes;
}
