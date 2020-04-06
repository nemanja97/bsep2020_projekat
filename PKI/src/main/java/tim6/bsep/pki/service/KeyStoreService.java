package tim6.bsep.pki.service;

import tim6.bsep.pki.keystore.KeyStoreReader;
import tim6.bsep.pki.keystore.KeyStoreWriter;

public class KeyStoreService {

    private KeyStoreReader keyStoreReader;
    private KeyStoreWriter keyStoreWriter;

    public void createKeyStore(String path, String filename, char[] password) {
        keyStoreWriter.createKeyStore(path, filename, password);
    }

}
