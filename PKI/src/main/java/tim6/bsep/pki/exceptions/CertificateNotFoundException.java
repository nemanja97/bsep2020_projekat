package tim6.bsep.pki.exceptions;

public class CertificateNotFoundException extends Exception {

    public CertificateNotFoundException(String id) {
        super("Certificate: " + id + "not found");
    }

    public CertificateNotFoundException(String id, Throwable cause) {
        super("Certificate serial number: " + id + " not found", cause);
    }

}
