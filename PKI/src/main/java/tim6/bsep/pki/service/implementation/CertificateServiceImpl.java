package tim6.bsep.pki.service.implementation;

import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tim6.bsep.pki.exceptions.*;
import tim6.bsep.pki.generator.CertificateGenerator;
import tim6.bsep.pki.generator.KeyPairGenerator;
import tim6.bsep.pki.model.CertificateInfo;
import tim6.bsep.pki.model.IssuerData;
import tim6.bsep.pki.model.RevocationReason;
import tim6.bsep.pki.model.SubjectData;
import tim6.bsep.pki.service.CertificateInfoService;
import tim6.bsep.pki.service.CertificateService;
import tim6.bsep.pki.service.KeyStoreService;

import java.io.IOException;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private KeyStoreService keyStoreService;

    @Autowired
    private CertificateInfoService certificateInfoService;

    public X509Certificate createSelfSignedCertificate() {
        KeyPair keyPair = KeyPairGenerator.generateKeyPair();
        return null;
    }

    public X509Certificate createCertificate(String issuerAlias, String alias, X500Name subjectName, String template) throws CertificateNotFoundException, IssuerNotCAException, IssuerNotValidException, UnknownTemplateException, AliasAlreadyTakenException {
        keyStoreService.loadKeyStore();
        Certificate[] issuerCertificateChain = keyStoreService.readCertificateChain(issuerAlias);
        IssuerData issuerData = keyStoreService.readIssuerFromStore(issuerAlias);

        X509Certificate issuer = (X509Certificate) issuerCertificateChain[0];
        if (!isCertificateValid(issuerAlias))
            throw new IssuerNotValidException();

        try {
            if (issuer.getBasicConstraints() == -1 || !issuer.getKeyUsage()[5]) {
                // Sertifikat nije CA
                // https://stackoverflow.com/questions/12092457/how-to-check-if-x509certificate-is-ca-certificate
                throw new IssuerNotCAException();
            }
        } catch (NullPointerException ignored) {
        }

        CertificateInfo certInfo = certificateInfoService.findByAlias(alias);
        if (certInfo != null && certInfo.getAlias().equals(alias))
            throw new AliasAlreadyTakenException();

        KeyPair keyPair = KeyPairGenerator.generateKeyPair();
        SubjectData subjectData = new SubjectData();
        subjectData.setX500name(subjectName);

        switch (template) {
            case "INTERMEDIATE_CA":
                createX509Certificate_INTERMEDIATE_CA(issuerAlias, alias, issuerCertificateChain, issuerData, keyPair, subjectData); return null;
            case "TLS_SERVER":
                createX509Certificate_TLS_SERVER(issuerAlias, alias, issuerCertificateChain, issuerData, keyPair, subjectData); return null;
            case "SIEM_CENTER":
                createX509Certificate_SIEM_CENTER(issuerAlias, alias, issuerCertificateChain, issuerData, keyPair, subjectData); return null;
            case "SIEM_AGENT":
                createX509Certificate_SIEM_AGENT(issuerAlias, alias, issuerCertificateChain, issuerData, keyPair, subjectData); return null;
            default:
                throw new UnknownTemplateException(template);
        }
    }

    private void createX509Certificate_INTERMEDIATE_CA(
            String issuerAlias, String alias, Certificate[] issuerCertificateChain, IssuerData issuerData, KeyPair keyPair, SubjectData subjectData) {

        Date[] dates;
        dates = generateStartAndEndDate(24);
        subjectData.setStartDate(dates[0]);
        subjectData.setEndDate(dates[1]);
        subjectData.setPublicKey(keyPair.getPublic());

        CertificateInfo certInfo = generateCertificateInfoEntity(subjectData, issuerAlias, alias, true);
        subjectData.setSerialNumber(certInfo.getId().toString());
        X509Certificate createdCertificate = CertificateGenerator.generateCertificate(subjectData, issuerData, "INTERMEDIATE_CA");
        Certificate[] newCertificateChain = ArrayUtils.addFirst(issuerCertificateChain, createdCertificate);
        keyStoreService.savePrivateKey(alias, newCertificateChain, keyPair.getPrivate());
        keyStoreService.saveKeyStore();
    }

    private void createX509Certificate_TLS_SERVER(
            String issuerAlias, String alias, Certificate[] issuerCertificateChain, IssuerData issuerData, KeyPair keyPair, SubjectData subjectData) {

        Date[] dates;
        dates = generateStartAndEndDate(12);
        subjectData.setStartDate(dates[0]);
        subjectData.setEndDate(dates[1]);
        subjectData.setPublicKey(keyPair.getPublic());

        CertificateInfo certInfo = generateCertificateInfoEntity(subjectData, issuerAlias, alias, false);
        subjectData.setSerialNumber(certInfo.getId().toString());
        X509Certificate createdCertificate = CertificateGenerator.generateCertificate(subjectData, issuerData, "TLS_SERVER");
        Certificate[] newCertificateChain = ArrayUtils.addFirst(issuerCertificateChain, createdCertificate);
        keyStoreService.savePrivateKey(alias, newCertificateChain, keyPair.getPrivate());
        keyStoreService.saveKeyStore();
    }

    private void createX509Certificate_SIEM_CENTER(
            String issuerAlias, String alias, Certificate[] issuerCertificateChain, IssuerData issuerData, KeyPair keyPair, SubjectData subjectData) {

        Date[] dates;
        dates = generateStartAndEndDate(12);
        subjectData.setStartDate(dates[0]);
        subjectData.setEndDate(dates[1]);
        subjectData.setPublicKey(keyPair.getPublic());

        CertificateInfo certInfo = generateCertificateInfoEntity(subjectData, issuerAlias, alias, false);
        subjectData.setSerialNumber(certInfo.getId().toString());
        X509Certificate createdCertificate = CertificateGenerator.generateCertificate(subjectData, issuerData, "SIEM_CENTER");
        Certificate[] newCertificateChain = ArrayUtils.addFirst(issuerCertificateChain, createdCertificate);
        keyStoreService.savePrivateKey(alias, newCertificateChain, keyPair.getPrivate());
        keyStoreService.saveKeyStore();
    }

    private void createX509Certificate_SIEM_AGENT(
            String issuerAlias, String alias, Certificate[] issuerCertificateChain, IssuerData issuerData, KeyPair keyPair, SubjectData subjectData) {

        Date[] dates;
        dates = generateStartAndEndDate(12);
        subjectData.setStartDate(dates[0]);
        subjectData.setEndDate(dates[1]);
        subjectData.setPublicKey(keyPair.getPublic());

        CertificateInfo certInfo = generateCertificateInfoEntity(subjectData, issuerAlias, alias, false);
        subjectData.setSerialNumber(certInfo.getId().toString());
        X509Certificate createdCertificate = CertificateGenerator.generateCertificate(subjectData, issuerData, "SIEM_AGENT");
        Certificate[] newCertificateChain = ArrayUtils.addFirst(issuerCertificateChain, createdCertificate);
        keyStoreService.savePrivateKey(alias, newCertificateChain, keyPair.getPrivate());
        keyStoreService.saveKeyStore();
    }

    @Override
    public String writeCertificateToPEM(X509Certificate certificate) throws CertificateEncodingException, IOException {
        StringWriter writer = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(writer);
        pemWriter.writeObject(certificate);
        pemWriter.flush();
        pemWriter.close();
        return writer.toString();
    }

    private Date[] generateStartAndEndDate(int months){
        Date startDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.MONTH, months);
        Date endDate = calendar.getTime();
        return new Date[] {startDate, endDate};
    }

    private CertificateInfo generateCertificateInfoEntity(SubjectData subjectData, String issuerAlias, String alias, Boolean isCA){
        CertificateInfo certInfo = new CertificateInfo();
        String cn = subjectData.getX500name().getRDNs(BCStyle.CN)[0].getFirst().getValue().toString();

        certInfo.setAlias(alias);
        certInfo.setCommonName(cn);
        certInfo.setIssuerAlias(issuerAlias);
        certInfo.setStartDate(subjectData.getStartDate());
        certInfo.setEndDate(subjectData.getEndDate());
        certInfo.setRevoked(false);
        certInfo.setRevocationReason("");
        certInfo.setCA(isCA);
        return certificateInfoService.save(certInfo);
    }

    public boolean revokeCertificate(Long id, RevocationReason reason) {
        try {
            CertificateInfo certInfo = certificateInfoService.revoke(id, reason);
            return certInfo.isRevoked();
        } catch (CertificateNotFoundException e) {
            return false;
        }
    }

    public boolean isCertificateValid(String alias) {
        Certificate[] chain = keyStoreService.readCertificateChain(alias);

        if (chain == null){
            return false;
        }

        Date now = new Date();
        X509Certificate x509cert;
        for (int i = chain.length - 1; i >= 0; i--) {
            x509cert = (X509Certificate) chain[i];

            CertificateInfo certificateInfo = certificateInfoService.findById(x509cert.getSerialNumber().longValue());

            if(certificateInfo.isRevoked()){
                return false;
            }

            if(now.after(x509cert.getNotAfter()) || now.before(x509cert.getNotBefore())){
                return false;
            }

            try {
                if(i == 0){
                    return isSelfSigned(x509cert);
                }
                X509Certificate issuer = (X509Certificate) chain[i-1];
                x509cert.verify(issuer.getPublicKey());
            } catch (SignatureException | InvalidKeyException e) {
                return false;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    @Override
    public X509Certificate findByAlias(String alias) {
        keyStoreService.loadKeyStore();
        return (X509Certificate) keyStoreService.readCertificate(alias);
    }

    public static boolean isSelfSigned(X509Certificate cert) {
        try {
            cert.verify(cert.getPublicKey());
            return true;
        } catch (SignatureException | InvalidKeyException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
