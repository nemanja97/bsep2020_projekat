package tim6.bsep.pki.service;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.operator.OperatorCreationException;
import tim6.bsep.pki.exceptions.CertificateNotFoundException;
import tim6.bsep.pki.exceptions.IssuerNotCAException;
import tim6.bsep.pki.model.RevocationReason;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public interface CertificateService {

    X509Certificate createCertificate(String issuerAlias, X500Name subjectName, boolean isCa) throws CertificateNotFoundException, IssuerNotCAException;

    String writeCertificateToPEM(String id) throws CertificateEncodingException, IOException;

    public boolean isCertificateValid(String id);

    public boolean isCertificateValid(X509Certificate certificate);

    boolean revokeCertificate(Long id, RevocationReason reason) throws CertificateException, OperatorCreationException;
}
