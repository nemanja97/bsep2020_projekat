package tim6.bsep.pki.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    @Temporal(TemporalType.TIMESTAMP)
    private Date revocationDate;

    private boolean isCA;

    @Enumerated(EnumType.STRING)
    private Template template;

    public CertificateInfo(){ }


}
