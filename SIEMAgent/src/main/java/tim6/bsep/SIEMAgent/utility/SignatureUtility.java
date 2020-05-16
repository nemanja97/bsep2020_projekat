package tim6.bsep.SIEMAgent.utility;

import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import tim6.bsep.SIEMAgent.keystore.KeyStoreReader;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class SignatureUtility {

    public static byte[] signMessage(String msg, String keyStoreName, String keyStorePassword, String certAlias) throws OperatorCreationException, CertificateEncodingException, IOException, CMSException {
        byte[] data = msg.getBytes();
        KeyStoreReader ksReader= new KeyStoreReader();
        X509Certificate certificate = (X509Certificate) ksReader.readCertificate(keyStoreName, keyStorePassword, certAlias);
        PrivateKey privateKey = ksReader.readPrivateKey(keyStoreName, keyStorePassword, certAlias, keyStorePassword);
        Security.addProvider(new BouncyCastleProvider());

        List<X509Certificate> certList = new ArrayList<X509Certificate>();
        certList.add(certificate);
        Store certificateStore = new JcaCertStore(certList);

        CMSTypedData cmsData = new CMSProcessableByteArray(data);
        CMSSignedDataGenerator cmsGenerator = new CMSSignedDataGenerator();

        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(privateKey);
        cmsGenerator.addSignerInfoGenerator(
                new JcaSignerInfoGeneratorBuilder(
                    new JcaDigestCalculatorProviderBuilder().setProvider("BC").build())
                .build(contentSigner, certificate));
        cmsGenerator.addCertificates(certificateStore);

        CMSSignedData cms = cmsGenerator.generate(cmsData, true);
        byte[] signedMessage = cms.getEncoded();
        return signedMessage;

    }
}
