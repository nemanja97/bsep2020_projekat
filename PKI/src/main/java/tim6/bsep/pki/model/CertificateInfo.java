package tim6.bsep.pki.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity
public class CertificateInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String alias;

    private String issuerAlias;

    private String commonName;

    private Date startDate;

    private Date endDate;

    private boolean revoked;

    private String revocationReason;

    private boolean isCA;

    public CertificateInfo(){ }


}
