package tim6.bsep.pki.web.v1.dto;

import lombok.Getter;
import lombok.Setter;
import tim6.bsep.pki.model.RevocationReason;

@Getter
@Setter
public class CertificateRevokeDTO {

    private String alias;

    private RevocationReason revocationReason;
}
