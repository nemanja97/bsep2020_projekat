package tim6.bsep.pki.service;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.operator.OperatorCreationException;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public interface CertificateService {

    X509Certificate createCertificate(String issuerAlias, X500Name subjectName, boolean isCa);

    public boolean isCertificateValid(String id);

    public boolean isCertificateValid(X509Certificate certificate);

    public boolean revokeCertificate(String id, CRLReason reason) throws CertificateException, OperatorCreationException;
}
