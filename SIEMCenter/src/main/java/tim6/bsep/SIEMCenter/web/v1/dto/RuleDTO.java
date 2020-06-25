package tim6.bsep.SIEMCenter.web.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tim6.bsep.SIEMCenter.model.Rule;
import tim6.bsep.SIEMCenter.model.enums.SeverityLevel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RuleDTO {

    private long id;

    @NotBlank(message = "Name can't be empty")
    private String name;

    @NotBlank(message = "Content can't be empty")
    private String content;

    @NotNull(message = "Produced severity level can't be null")
    private SeverityLevel produces;

    @NotNull(message = "Consumes severity level can't be null")
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
