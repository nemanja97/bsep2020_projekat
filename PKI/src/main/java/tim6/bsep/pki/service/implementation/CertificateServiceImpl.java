package tim6.bsep.pki.service.implementation;

import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509v2CRLBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tim6.bsep.pki.exceptions.CertificateNotFoundException;
import tim6.bsep.pki.exceptions.IssuerNotCAException;
import tim6.bsep.pki.generator.CertificateGenerator;
import tim6.bsep.pki.generator.KeyPairGenerator;
import tim6.bsep.pki.model.CertificateInfo;
import tim6.bsep.pki.model.IssuerData;
import tim6.bsep.pki.model.RevocationReason;
import tim6.bsep.pki.model.SubjectData;
import tim6.bsep.pki.service.CertificateInfoService;
import tim6.bsep.pki.service.CertificateService;
import tim6.bsep.pki.service.KeyStoreService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

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

    public X509Certificate createCertificate(String issuerAlias, X500Name subjectName, boolean isCa) throws CertificateNotFoundException, IssuerNotCAException {
        keyStoreService.loadKeyStore();
        Certificate[] issuerCertificateChain = keyStoreService.readCertificateChain(issuerAlias);
        IssuerData issuerData = keyStoreService.readIssuerFromStore(issuerAlias);

        X509Certificate issuer = (X509Certificate) issuerCertificateChain[issuerCertificateChain.length - 1];

        try {
            if (issuer.getBasicConstraints() == -1 || !issuer.getKeyUsage()[5]) {
                // Sertifikat nije CA
                // https://stackoverflow.com/questions/12092457/how-to-check-if-x509certificate-is-ca-certificate
                throw new IssuerNotCAException();
            }
        } catch (NullPointerException ignored) {
        }

        KeyPair keyPair = KeyPairGenerator.generateKeyPair();
        SubjectData subjectData = new SubjectData();
        subjectData.setX500name(subjectName);
        Date[] dates;
        if(isCa){
            dates = generateStartAndEndDate(12);
        }else{
            dates = generateStartAndEndDate(6);

        }
        subjectData.setStartDate(dates[0]);
        subjectData.setEndDate(dates[1]);
        subjectData.setPublicKey(keyPair.getPublic());

        CertificateInfo certInfo = generateCertificateInfoEntity(subjectData, issuerAlias);
        subjectData.setSerialNumber(certInfo.getId().toString());
        X509Certificate createdCertificate = CertificateGenerator.generateCertificate(subjectData, issuerData, isCa);
        Certificate[] newCertificateChain = ArrayUtils.addAll(issuerCertificateChain, createdCertificate);
        keyStoreService.savePrivateKey(subjectData.getSerialNumber(), newCertificateChain, keyPair.getPrivate());
        keyStoreService.saveKeyStore();
        return null;
    }

    private Date[] generateStartAndEndDate(int months){
        Date startDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.MONTH, months);
        Date endDate = calendar.getTime();
        return new Date[] {startDate, endDate};
    }

    private CertificateInfo generateCertificateInfoEntity(SubjectData subjectData, String issuerAlias){
        CertificateInfo certInfo = new CertificateInfo();
        String cn = subjectData.getX500name().getRDNs(BCStyle.CN)[0].getFirst().getValue().toString();

        certInfo.setCommonName(cn);
        certInfo.setIssuerID(Long.parseLong(issuerAlias));
        certInfo.setStartDate(subjectData.getStartDate());
        certInfo.setEndDate(subjectData.getEndDate());
        certInfo.setRevoked(false);
        certInfo.setRevocationReason("");
        return certificateInfoService.save(certInfo);
    }

    public boolean isCertificateValid() {
        return false;
    }

    public boolean revokeCertificate(Long id, RevocationReason reason) {
        try {
            CertificateInfo certInfo = certificateInfoService.revoke(id, reason);
            return certInfo.isRevoked();
        } catch (CertificateNotFoundException e) {
            return false;
        }
    }
}
