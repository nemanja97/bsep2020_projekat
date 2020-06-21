package tim6.bsep.SIEMCenter.model;

import com.querydsl.core.annotations.QueryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import tim6.bsep.SIEMCenter.model.enums.SeverityLevel;

@Document(collection = "rules")
@QueryEntity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rule {

    private long id;

    private String name;

    private String content;

    private SeverityLevel produces;

    private SeverityLevel consumes;

}
