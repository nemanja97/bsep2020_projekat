package tim6.bsep.pki.service;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.operator.OperatorCreationException;
import tim6.bsep.pki.exceptions.*;
import tim6.bsep.pki.model.RevocationReason;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public interface CertificateService {

    X509Certificate createCertificate(String issuerAlias, String alias, X500Name subjectName, String template) throws CertificateNotFoundException, IssuerNotCAException, IssuerNotValidException, UnknownTemplateException, AliasAlreadyTakenException;

    String writeCertificateToPEM(X509Certificate certificate) throws CertificateEncodingException, IOException;

    String writePrivateKeyToPEM(PrivateKey privateKey) throws CertificateEncodingException, IOException;

    public boolean isCertificateValid(String alias);

    X509Certificate findByAlias(String alias);

    boolean revokeCertificate(Long id, RevocationReason reason) throws CertificateException, OperatorCreationException;
}
