package tim6.bsep.SIEMCenter.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.SerializationUtils;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import tim6.bsep.SIEMCenter.keystore.KeyStoreReader;
import tim6.bsep.SIEMCenter.model.Log;
import tim6.bsep.SIEMCenter.model.LogList;
import tim6.bsep.SIEMCenter.web.v1.dto.LogDTO;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SignatureUtility {

    public static boolean checkMessage(byte[] signedMessage){
        try {
            KeyStoreReader keyStoreReader = new KeyStoreReader();
            X509Certificate msgCertificate = SignatureUtility.extractCertificate(signedMessage);
            String alias = keyStoreReader.readCertificateAlias("truststore.jks","secret", msgCertificate);
            X509Certificate trustedStoreCertificate = (X509Certificate) keyStoreReader.readCertificateFromTrustStore("truststore.jks", "secret", alias);
            return SignatureUtility.verifySignature(signedMessage, trustedStoreCertificate);
        }catch (Exception e) {
            return false;
        }
    }

    public static byte[] signMessage(String msg, String keyStoreName, String keyStorePassword, String certAlias) throws OperatorCreationException, CertificateEncodingException, CMSException, IOException {
        byte[] data = msg.getBytes();
        KeyStoreReader ksReader= new KeyStoreReader();
        X509Certificate certificate = (X509Certificate) ksReader.readCertificateFromKeyStore(keyStoreName, keyStorePassword, certAlias);
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

    public static boolean verifySignature(byte[] msg, X509Certificate certificate) throws IOException, CMSException ,OperatorCreationException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(msg);
        ASN1InputStream asnInputStream = new ASN1InputStream(inputStream);
        CMSSignedData cmsSignedData = new CMSSignedData(ContentInfo.getInstance(asnInputStream.readObject()));
        SignerInformationStore signers = cmsSignedData.getSignerInfos();
        SignerInformation signer = signers.getSigners().iterator().next();

        return signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(certificate.getPublicKey()));
    }

    public static LogList extractMessage(byte[] signedMessage) throws IOException, CMSException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(signedMessage);
        ASN1InputStream asnInputStream = new ASN1InputStream(inputStream);
        CMSSignedData cmsSignedData = new CMSSignedData(ContentInfo.getInstance(asnInputStream.readObject()));
        CMSProcessable msg = cmsSignedData.getSignedContent();

        return logFromJson(new String((byte[]) msg.getContent()));
    }

    public static String extractMessageString(byte[] signedMessage) throws IOException, CMSException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(signedMessage);
        ASN1InputStream asnInputStream = new ASN1InputStream(inputStream);
        CMSSignedData cmsSignedData = new CMSSignedData(ContentInfo.getInstance(asnInputStream.readObject()));
        CMSProcessable msg = cmsSignedData.getSignedContent();
        return new String((byte[]) msg.getContent());
    }

    public static X509Certificate extractCertificate(byte[] signedMessage) throws IOException, CMSException, CertificateException {
        Security.addProvider(new BouncyCastleProvider());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(signedMessage);
        ASN1InputStream asnInputStream = new ASN1InputStream(inputStream);
        CMSSignedData cmsSignedData = new CMSSignedData(ContentInfo.getInstance(asnInputStream.readObject()));
        SignerInformationStore signers = cmsSignedData.getSignerInfos();
        SignerInformation signer = signers.getSigners().iterator().next();
        Store<?> certStore = cmsSignedData.getCertificates();
        Collection<?> certCollection = certStore.getMatches(signer.getSID());
        Iterator<?> certIt = certCollection.iterator();
        X509CertificateHolder certHolder = (X509CertificateHolder) certIt.next();
        return new JcaX509CertificateConverter().setProvider( "BC" ).getCertificate(certHolder);
    }

    private static LogList logFromJson(String log) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(log, LogList.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
