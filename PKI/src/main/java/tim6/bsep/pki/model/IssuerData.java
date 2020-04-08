package tim6.bsep.pki.model;

import java.security.PrivateKey;

import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.asn1.x500.X500Name;

@Getter
@Setter
public class IssuerData {

    private X500Name x500name;
    private PrivateKey privateKey;

    public IssuerData() {
    }

    public IssuerData(PrivateKey privateKey, X500Name x500name) {
        this.privateKey = privateKey;
        this.x500name = x500name;
    }

}