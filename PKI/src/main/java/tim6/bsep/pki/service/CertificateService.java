package tim6.bsep.pki.service;

import tim6.bsep.pki.generator.KeyPairGenerator;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

public class CertificateService {

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

    public boolean revokeCertificate() {
        return false;
    }
}
