package tim6.bsep.pki.service.implementation;

import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.io.pem.PemObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tim6.bsep.pki.exceptions.*;
import tim6.bsep.pki.generator.CertificateGenerator;
import tim6.bsep.pki.generator.KeyPairGenerator;
import tim6.bsep.pki.model.*;
import tim6.bsep.pki.service.CertificateInfoService;
import tim6.bsep.pki.service.CertificateService;
import tim6.bsep.pki.service.KeyStoreService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private KeyStoreService keyStoreService;

    @Autowired
    private CertificateInfoService certificateInfoService;

    public CertificateInfo createCertificate(String issuerAlias, String alias, X500Name subjectName, String template) throws CertificateNotFoundException, IssuerNotCAException, IssuerNotValidException, UnknownTemplateException, AliasAlreadyTakenException {
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

        CertificateInfo certInfo = certificateInfoService.findByAliasIgnoreCase(alias);
        if (certInfo != null)
            throw new AliasAlreadyTakenException();

        KeyPair keyPair = KeyPairGenerator.generateKeyPair();
        assert keyPair != null;

        SubjectData subjectData = new SubjectData();
        subjectData.setX500name(subjectName);

        switch (template) {
            case "INTERMEDIATE_CA":
                return createX509Certificate_INTERMEDIATE_CA(issuerAlias, alias, issuerCertificateChain, issuerData, keyPair, subjectData);
            case "TLS_SERVER":
                return createX509Certificate_TLS_SERVER(issuerAlias, alias, issuerCertificateChain, issuerData, keyPair, subjectData);
            case "SIEM_CENTER":
                return createX509Certificate_SIEM_CENTER(issuerAlias, alias, issuerCertificateChain, issuerData, keyPair, subjectData);
            case "SIEM_AGENT":
                return createX509Certificate_SIEM_AGENT(issuerAlias, alias, issuerCertificateChain, issuerData, keyPair, subjectData);
            default:
                throw new UnknownTemplateException(template);
        }
    }

    private CertificateInfo createX509Certificate_INTERMEDIATE_CA(
            String issuerAlias, String alias, Certificate[] issuerCertificateChain, IssuerData issuerData, KeyPair keyPair, SubjectData subjectData) {

        Date[] dates;
        dates = generateStartAndEndDate(24);
        subjectData.setStartDate(dates[0]);
        subjectData.setEndDate(dates[1]);
        subjectData.setPublicKey(keyPair.getPublic());

        CertificateInfo certInfo = generateCertificateInfoEntity(subjectData, issuerAlias, alias, true, Template.INTERMEDIATE_CA);
        subjectData.setSerialNumber(certInfo.getId().toString());
        X509Certificate createdCertificate = CertificateGenerator.generateCertificate(subjectData, issuerData, Template.INTERMEDIATE_CA, keyPair, false, issuerCertificateChain[0]);
        Certificate[] newCertificateChain = ArrayUtils.insert(0, issuerCertificateChain, createdCertificate);
        keyStoreService.savePrivateKey(alias, newCertificateChain, keyPair.getPrivate());
        keyStoreService.saveKeyStore();
        return certInfo;
    }

    private CertificateInfo createX509Certificate_TLS_SERVER(
            String issuerAlias, String alias, Certificate[] issuerCertificateChain, IssuerData issuerData, KeyPair keyPair, SubjectData subjectData) {

        Date[] dates;
        dates = generateStartAndEndDate(12);
        subjectData.setStartDate(dates[0]);
        subjectData.setEndDate(dates[1]);
        subjectData.setPublicKey(keyPair.getPublic());

        CertificateInfo certInfo = generateCertificateInfoEntity(subjectData, issuerAlias, alias, false, Template.TLS_SERVER);
        subjectData.setSerialNumber(certInfo.getId().toString());
        X509Certificate createdCertificate = CertificateGenerator.generateCertificate(subjectData, issuerData, Template.TLS_SERVER, keyPair, false, issuerCertificateChain[0]);
        Certificate[] newCertificateChain = ArrayUtils.insert(0, issuerCertificateChain, createdCertificate);
        keyStoreService.savePrivateKey(alias, newCertificateChain, keyPair.getPrivate());
        keyStoreService.saveKeyStore();
        return certInfo;
    }

    private CertificateInfo createX509Certificate_SIEM_CENTER(
            String issuerAlias, String alias, Certificate[] issuerCertificateChain, IssuerData issuerData, KeyPair keyPair, SubjectData subjectData) {

        Date[] dates;
        dates = generateStartAndEndDate(12);
        subjectData.setStartDate(dates[0]);
        subjectData.setEndDate(dates[1]);
        subjectData.setPublicKey(keyPair.getPublic());

        CertificateInfo certInfo = generateCertificateInfoEntity(subjectData, issuerAlias, alias, false, Template.SIEM_CENTER);
        subjectData.setSerialNumber(certInfo.getId().toString());
        X509Certificate createdCertificate = CertificateGenerator.generateCertificate(subjectData, issuerData, Template.SIEM_CENTER, keyPair, false, issuerCertificateChain[0]);
        Certificate[] newCertificateChain = ArrayUtils.insert(0, issuerCertificateChain, createdCertificate);
        keyStoreService.savePrivateKey(alias, newCertificateChain, keyPair.getPrivate());
        keyStoreService.saveKeyStore();
        return certInfo;
    }

    private CertificateInfo createX509Certificate_SIEM_AGENT(
            String issuerAlias, String alias, Certificate[] issuerCertificateChain, IssuerData issuerData, KeyPair keyPair, SubjectData subjectData) {

        Date[] dates;
        dates = generateStartAndEndDate(12);
        subjectData.setStartDate(dates[0]);
        subjectData.setEndDate(dates[1]);
        subjectData.setPublicKey(keyPair.getPublic());

        CertificateInfo certInfo = generateCertificateInfoEntity(subjectData, issuerAlias, alias, false, Template.SIEM_AGENT);
        subjectData.setSerialNumber(certInfo.getId().toString());
        X509Certificate createdCertificate = CertificateGenerator.generateCertificate(subjectData, issuerData, Template.SIEM_AGENT, keyPair, false, issuerCertificateChain[0]);
        Certificate[] newCertificateChain = ArrayUtils.insert(0, issuerCertificateChain, createdCertificate);
        keyStoreService.savePrivateKey(alias, newCertificateChain, keyPair.getPrivate());
        keyStoreService.saveKeyStore();
        return certInfo;
    }

    @Override
    public ByteArrayOutputStream getPemCertificateChainWithPrivateKey(String alias) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

        ZipEntry pemEntry = new ZipEntry("certificate.crt");
        ZipEntry keyEntry = new ZipEntry("certificate.key");

        zipOutputStream.putNextEntry(pemEntry);
        zipOutputStream.write(getPemCertificateChain(alias).getBytes());
        zipOutputStream.closeEntry();

        zipOutputStream.putNextEntry(keyEntry);
        zipOutputStream.write(getPemPrivateKey(alias).getBytes());
        zipOutputStream.closeEntry();

        zipOutputStream.finish();
        zipOutputStream.close();

        outputStream.flush();
        outputStream.close();

        return outputStream;
    }

    @Override
    public String getPemCertificateChain(String alias) throws IOException {
        Certificate[] chain = keyStoreService.readCertificateChain(alias);

        StringBuilder chainBuilder = new StringBuilder();
        for (Certificate c : chain) {
            String pemCertificate = writeCertificateToPEM((X509Certificate) c);
            chainBuilder.append(pemCertificate);
        }
        return chainBuilder.toString();
    }

    @Override
    public String getPemPrivateKey(String alias) throws IOException {
        PrivateKey privateKey = keyStoreService.readPrivateKey(alias);
        return writePrivateKeyToPEM(privateKey);
    }

    private Date[] generateStartAndEndDate(int months) {
        Date startDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.MONTH, months);
        Date endDate = calendar.getTime();
        return new Date[]{startDate, endDate};
    }

    private CertificateInfo generateCertificateInfoEntity(SubjectData subjectData, String issuerAlias, String alias, Boolean isCA, Template template) {
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
        certInfo.setTemplate(template);
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

        if (chain == null) {
            return false;
        }

        Date now = new Date();
        X509Certificate x509cert;
        for (int i = 0; i < chain.length; i++) {
            x509cert = (X509Certificate) chain[i];

            CertificateInfo certificateInfo = certificateInfoService.findById(x509cert.getSerialNumber().longValue());

            if (certificateInfo.isRevoked()) {
                return false;
            }

            if (now.after(x509cert.getNotAfter()) || now.before(x509cert.getNotBefore())) {
                return false;
            }

            try {
                if (i == chain.length - 1) {
                    return isSelfSigned(x509cert);
                }
                X509Certificate issuer = (X509Certificate) chain[i + 1];
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

    public String writeCertificateToPEM(X509Certificate certificate) throws IOException {
        StringWriter writer = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(writer);
        pemWriter.writeObject(certificate);
        pemWriter.flush();
        pemWriter.close();
        return writer.toString();
    }

    public String writePrivateKeyToPEM(PrivateKey privateKey) throws IOException {
        PemObject pemFile = new PemObject("PRIVATE KEY", privateKey.getEncoded());

        StringWriter writer = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(writer);
        pemWriter.writeObject(pemFile);
        pemWriter.flush();
        pemWriter.close();
        return writer.toString();
    }

}
