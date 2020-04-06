package tim6.bsep.pki.service;

import org.springframework.beans.factory.annotation.Value;
import tim6.bsep.pki.keystore.KeyStoreReader;
import tim6.bsep.pki.keystore.KeyStoreWriter;
import tim6.bsep.pki.model.IssuerData;

import java.security.cert.Certificate;

public class KeyStoreService {

    @Value("${PKI.keystore_path}")
    public String keystore_path;

    @Value("${PKI.keystore_password}")
    public String keystore_password;

    private KeyStoreReader keyStoreReader;
    private KeyStoreWriter keyStoreWriter;

    public void createKeyStore(String path, String filename, char[] password) {
        keyStoreWriter.createKeyStore(path, filename, password);
    }

    public Certificate readCertificate(String keyStoreFile, String password, String alias) {
        return keyStoreReader.readCertificate(keyStoreFile, password, alias);
    }

    public IssuerData readIssuerFromStore(String keyStoreFile, String alias, char[] password, char[] keyPass) {
        return keyStoreReader.readIssuerFromStore(keyStoreFile, alias, password, keyPass);
    }

}
