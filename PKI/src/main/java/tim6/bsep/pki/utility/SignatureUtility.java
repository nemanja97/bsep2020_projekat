package tim6.bsep.pki.utility;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tim6.bsep.pki.service.KeyStoreService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Component
public class SignatureUtility {

    @Autowired
    private KeyStoreService keyStoreService;

    private final String serverAlias = "PKI";

    public byte[] signMessage(String msg) throws OperatorCreationException, CertificateEncodingException, CMSException, IOException {
        byte[] data = msg.getBytes();
        X509Certificate certificate = (X509Certificate) keyStoreService.readCertificate(serverAlias);
        PrivateKey privateKey = keyStoreService.readPrivateKey(serverAlias);
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

    public boolean verifySignature(byte[] msg, String certAlias) throws IOException, CMSException ,OperatorCreationException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(msg);
        ASN1InputStream asnInputStream = new ASN1InputStream(inputStream);
        CMSSignedData cmsSignedData = new CMSSignedData(ContentInfo.getInstance(asnInputStream.readObject()));
        SignerInformationStore signers = cmsSignedData.getSignerInfos();
        SignerInformation signer = signers.getSigners().iterator().next();

        X509Certificate certificate = (X509Certificate) keyStoreService.readCertificate(certAlias);
        return signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(certificate.getPublicKey()));
    }

    public String extractMessage(byte[] msg) throws IOException, CMSException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(msg);
        ASN1InputStream asnInputStream = new ASN1InputStream(inputStream);
        CMSSignedData cmsSignedData = new CMSSignedData(ContentInfo.getInstance(asnInputStream.readObject()));
        CMSProcessable alias = cmsSignedData.getSignedContent();
        return new String((byte[]) alias.getContent());
    }
}
