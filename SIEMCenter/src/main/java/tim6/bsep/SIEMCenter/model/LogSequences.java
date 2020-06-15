package tim6.bsep.SIEMCenter.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "logSequences")
@Getter
@Setter
public class LogSequences {

    @Id
    private String id;
    private long seq;

    public LogSequences(String id, long seq) {
        this.id = id;
        this.seq = seq;
    }

    public LogSequences() {
    }
}
