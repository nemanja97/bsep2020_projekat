package tim6.bsep.pki.service;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v2CRLBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import tim6.bsep.pki.generator.KeyPairGenerator;
import tim6.bsep.pki.model.IssuerData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class CertificateService {

    @Autowired
    private KeyStoreService keyStoreService;

    public X509Certificate createSelfSignedCertificate() {
        KeyPair keyPair = KeyPairGenerator.generateKeyPair();
        return null;
    }

    public X509Certificate createIntermediaryCACertificate() {
        return null;
    }

    public X509Certificate createLeafCertificate() {
        return null;
    }

    public boolean isCertificateValid() {
        return false;
    }

    public boolean revokeCertificate(String id, CRLReason reason) throws CertificateException, OperatorCreationException {
        X509Certificate certificate = (X509Certificate) keyStoreService.readCertificate(
                keyStoreService.keystore_path,
                keyStoreService.keystore_password,
                id);
        if (certificate == null)
            throw new CertificateException("Certificate doesn't exist");

        X509Certificate ca = (X509Certificate) keyStoreService.readCertificate(
                keyStoreService.keystore_path,
                keyStoreService.keystore_password,
                certificate.getIssuerX500Principal().getName()
        );
        if (ca == null)
            throw new CertificateException("Certificate authority doesn't exist");

        IssuerData issuerData = keyStoreService.readIssuerFromStore(
                keyStoreService.keystore_path,
                ca.getSubjectX500Principal().getName(),
                keyStoreService.keystore_password.toCharArray(),
                ca.getPublicKey().toString().toCharArray()
        );

        Date now = new Date();
        X509v2CRLBuilder crlBuilder = new X509v2CRLBuilder(issuerData.getX500name(), now);
        crlBuilder.addCRLEntry((certificate).getSerialNumber(), now, Extensions.getInstance(reason.getValue()));

        ContentSigner sigGen = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").
                build(issuerData.getPrivateKey());
        X509CRLHolder crlHolder = crlBuilder.build(sigGen);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("static/crls/" + ca.getSubjectX500Principal().getName() + ".crl");
            fileOutputStream.write(crlHolder.getEncoded());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
