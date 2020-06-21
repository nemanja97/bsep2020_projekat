package tim6.bsep.SIEMCenter.web.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tim6.bsep.SIEMCenter.model.Whitelist;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WhitelistDTO {

    private long id;

    @NotBlank
    private String name;

    @NotEmpty
    private List<String> content;

    public WhitelistDTO(Whitelist whitelist) {
        id = whitelist.getId();
        name = whitelist.getName();
        content = whitelist.getContent();
    }
}

