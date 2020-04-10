package tim6.bsep.pki.web.v1.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class CertificateDTO {

    private Long id;

    private String alias;

    private String commonName;

    private String organization;

    private String organizationUnit;

    private String country;

    private String email;

    private String issuer_commonName;

    private String issuer_organization;

    private String issuer_organizationUnit;

    private String issuer_country;

    private String issuer_email;

    private boolean isCa;

    private HashMap<String, String> extensions;

    public CertificateDTO() {
    }
}
