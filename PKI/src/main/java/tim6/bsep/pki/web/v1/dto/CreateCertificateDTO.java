package tim6.bsep.pki.web.v1.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class CreateCertificateDTO {

    private String issuerAlias;

    private String commonName;

    private String organization;

    private String organizationUnit;

    private String country;

    private String email;

    private HashMap<String, String> extensions;

}
