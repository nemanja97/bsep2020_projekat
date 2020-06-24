package tim6.bsep.SIEMCenter.web.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tim6.bsep.SIEMCenter.model.Rule;
import tim6.bsep.SIEMCenter.model.enums.SeverityLevel;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RuleDTO {

    private long id;

    private String name;

    private String content;

    private SeverityLevel produces;

    private SeverityLevel consumes;

    public RuleDTO(Rule rule) {
        id = rule.getId();
        name = rule.getName();
        content = rule.getContent();
        produces = rule.getProduces();
        consumes = rule.getConsumes();
    }

    @Override
    public String toString() {
        return "RuleDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", produces=" + produces +
                ", consumes=" + consumes +
                '}';
    }
}
