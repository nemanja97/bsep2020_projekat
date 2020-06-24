package tim6.bsep.SIEMCenter.model.sequences;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ruleSequences")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RuleSequences {

    @Id
    private String id;
    private long seq;

}
