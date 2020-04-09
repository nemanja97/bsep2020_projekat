package tim6.bsep.pki.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CertificateInfoWithChildren {

    private Long id;

    private Long issuerId;

    private List<CertificateInfoWithChildren> issued;

    private String commonName;

    private Date startDate;

    private Date endDate;

    private boolean revoked;

    private String revocationReason;

    public CertificateInfoWithChildren(CertificateInfo certificateInfo) {
        this.id = certificateInfo.getId();
        this.issuerId = certificateInfo.getIssuerID();
        this.issued = new ArrayList<>();
        this.commonName = certificateInfo.getCommonName();
        this.startDate = certificateInfo.getStartDate();
        this.endDate = certificateInfo.getEndDate();
        this.revoked = certificateInfo.isRevoked();
        this.revocationReason = certificateInfo.getRevocationReason();    }

}
