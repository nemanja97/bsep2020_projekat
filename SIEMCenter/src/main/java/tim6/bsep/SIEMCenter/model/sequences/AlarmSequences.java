package tim6.bsep.SIEMCenter.model.sequences;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "alarmSequences")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmSequences {

    @Id
    private String id;
    private long seq;

}
