package tim6.bsep.pki.service;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.operator.OperatorCreationException;
import tim6.bsep.pki.exceptions.*;
import tim6.bsep.pki.model.CertificateInfo;
import tim6.bsep.pki.model.RevocationReason;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public interface CertificateService {

    CertificateInfo createCertificate(String issuerAlias, String alias, X500Name subjectName, String template) throws CertificateNotFoundException, IssuerNotCAException, IssuerNotValidException, UnknownTemplateException, AliasAlreadyTakenException;

    ByteArrayOutputStream getPemCertificateChainWithPrivateKey(String alias) throws IOException, CertificateEncodingException;

    String getPemCertificateChain(String alias) throws IOException, CertificateEncodingException;

    String getPemPrivateKey(String alias) throws IOException, CertificateEncodingException;

    boolean isCertificateValid(String alias);

    X509Certificate findByAlias(String alias);

    boolean revokeCertificate(Long id, RevocationReason reason) throws CertificateException, OperatorCreationException;
}
