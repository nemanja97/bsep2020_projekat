package tim6.bsep.pki.startup;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import tim6.bsep.pki.generator.CertificateGenerator;
import tim6.bsep.pki.generator.KeyPairGenerator;
import tim6.bsep.pki.model.CertificateInfo;
import tim6.bsep.pki.model.IssuerData;
import tim6.bsep.pki.model.SubjectData;
import tim6.bsep.pki.service.CertificateInfoService;
import tim6.bsep.pki.service.KeyStoreService;
import tim6.bsep.pki.service.implementation.KeyStoreServiceImpl;

import java.io.File;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Calendar;
import java.util.Date;

@Component
public class RootCertificateAuthorityInit implements ApplicationRunner {

    @Autowired
    private KeyStoreService keyStoreService;

    @Autowired
    private CertificateInfoService certificateInfoService;

    @Value("${PKI.keystore_path}")
    public String keystore_path;

    @Value("${PKI.keystore_name}")
    public String keystore_name;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        File keystore = new File(this.keystore_path + this.keystore_name);
        if(!keystore.exists()){
            keyStoreService.createKeyStore();
        }else {
            keyStoreService.loadKeyStore();
        }
        Certificate root = keyStoreService.readCertificate("1");
        if(root == null){
            createRootCA();
            keyStoreService.saveKeyStore();
        }
    }

    private void createRootCA(){
        X500Name rootName = generateRootInfo();
        KeyPair keyPair = KeyPairGenerator.generateKeyPair();
        SubjectData subjectData = new SubjectData();
        IssuerData issuerData = new IssuerData();

        issuerData.setX500name(rootName);
        issuerData.setPrivateKey(keyPair.getPrivate());

        subjectData.setX500name(rootName);
        subjectData.setPublicKey(keyPair.getPublic());

        Date[] dates = generateStartAndEndDate();
        subjectData.setStartDate(dates[0]);
        subjectData.setEndDate(dates[1]);

        CertificateInfo certificateInfo = generateCertificateInfoEntity(subjectData);
        subjectData.setSerialNumber(certificateInfo.getId().toString());
        Certificate rootCertificate = CertificateGenerator.generateCertificate(subjectData, issuerData, true);
        keyStoreService.savePrivateKey(subjectData.getSerialNumber(), new Certificate[]{rootCertificate}, keyPair.getPrivate());
    }

    private Date[] generateStartAndEndDate(){
        Date startDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.YEAR, 5);
        Date endDate = calendar.getTime();
        return new Date[] {startDate, endDate};
    }

    private X500Name generateRootInfo(){
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, "root");
        builder.addRDN(BCStyle.O, "BSEP-Tim6");
        builder.addRDN(BCStyle.OU, "CA");
        builder.addRDN(BCStyle.C, "RS");
        builder.addRDN(BCStyle.E, "tim6@info.com");
        return builder.build();
    }

    private CertificateInfo generateCertificateInfoEntity(SubjectData subjectData){
        CertificateInfo certInfo = new CertificateInfo();

        certInfo.setCommonName("root");
        certInfo.setStartDate(subjectData.getStartDate());
        certInfo.setEndDate(subjectData.getEndDate());
        certInfo.setRevoked(false);
        certInfo.setRevocationReason("");
        certInfo = certificateInfoService.save(certInfo);
        certInfo.setIssuerID(certInfo.getId());
        return certificateInfoService.save(certInfo);
    }
}
