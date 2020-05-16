package tim6.bsep.SIEMCenter.keystore;

import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class KeyStoreReader {

    /**
     * Ucitava sertifikat is KS fajla
     */
    public java.security.cert.Certificate readCertificateFromKeyStore(String keyStore, String keyStorePass, String alias) {
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");;
            File keyStoreFile = new ClassPathResource(keyStore).getFile();
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if(ks.isKeyEntry(alias)) {
                Certificate[] cert = ks.getCertificateChain(alias);
                return cert[0];
            }
        } catch (KeyStoreException | NoSuchProviderException | FileNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public java.security.cert.Certificate readCertificateFromTrustStore(String trustStore, String keyStorePass, String alias) {
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");;
            File keyStoreFile = new ClassPathResource(trustStore).getFile();
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            return ks.getCertificate(alias);
        } catch (KeyStoreException | NoSuchProviderException | FileNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Ucitava privatni kljuc is KS fajla
     */
    public PrivateKey readPrivateKey(String keyStore, String keyStorePass, String alias, String pass) {
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            File keyStoreFile = new ClassPathResource(keyStore).getFile();
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if(ks.isKeyEntry(alias)) {
                PrivateKey pk = (PrivateKey) ks.getKey(alias, pass.toCharArray());
                return pk;
            }
        } catch (KeyStoreException | NoSuchProviderException | FileNotFoundException | CertificateException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readCertificateAlias(String keyStore, String keyStorePass, Certificate certificate){
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            File keyStoreFile = new ClassPathResource(keyStore).getFile();
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());
            return ks.getCertificateAlias(certificate);
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }
}
