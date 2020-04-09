package tim6.bsep.pki.service;

import org.springframework.beans.factory.annotation.Value;
import tim6.bsep.pki.exceptions.CertificateNotFoundException;
import tim6.bsep.pki.model.IssuerData;

import java.security.PrivateKey;
import java.security.cert.Certificate;

public interface KeyStoreService {

    String keystore_path = null;
    
    String keystore_name = null;
    
    String keystore_password = null;

    void createKeyStore();

    void createKeyStore(String path, String filename, char[] password);

    void loadKeyStore();

    void saveKeyStore();

    void saveCertificate(String alias, Certificate certificate);

    void savePrivateKey(String alias, Certificate[] certificate, PrivateKey privateKey);

    PrivateKey readPrivateKey(String alias);

    Certificate readCertificate(String keyStoreFile, String password, String alias);

    Certificate[] readCertificateChain(String alias);

    Certificate readCertificate(String alias);

    IssuerData readIssuerFromStore(String keyStoreFile, String alias, char[] password, char[] keyPass) throws CertificateNotFoundException;

    IssuerData readIssuerFromStore(String alias) throws CertificateNotFoundException;
}
