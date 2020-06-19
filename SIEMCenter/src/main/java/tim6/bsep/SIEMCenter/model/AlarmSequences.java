package tim6.bsep.SIEMCenter.model;

import com.querydsl.core.annotations.QueryEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@QueryEntity
@Document(collection = "alarmSequences")
@Getter
@Setter
public class AlarmSequences {

    @Id
    private String id;
    private long seq;

    public AlarmSequences(String id, long seq) {
        this.id = id;
        this.seq = seq;
    }

    public AlarmSequences() {
    }
}
