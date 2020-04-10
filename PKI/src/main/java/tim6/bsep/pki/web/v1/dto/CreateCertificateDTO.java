package tim6.bsep.pki.web.v1.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;

@Getter
@Setter
public class CreateCertificateDTO {

    @NotBlank(message = "Issuer alias can't be empty")
    private String issuerAlias;

    @NotBlank(message = "Alias can't be empty")
    private String alias;

    @NotBlank(message = "Common name can't be empty")
    private String commonName;

    @NotBlank(message = "Organization can't be empty")
    private String organization;

    @NotBlank(message = "Organization unit can't be empty")
    private String organizationUnit;

    @NotBlank(message = "Country can't be empty")
    private String country;

    @NotBlank(message = "Email can't be empty")
    private String email;

    @NotBlank(message = "Template can't be empty")
    private String template;

}
