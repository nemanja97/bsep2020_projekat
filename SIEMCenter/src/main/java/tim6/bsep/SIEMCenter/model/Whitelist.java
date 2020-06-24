package tim6.bsep.SIEMCenter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "whitelists")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Whitelist {

    private long id;

    private String name;

    private List<String> content;
}
