package tim6.bsep.pki.exceptions;

public class IssuerNotCAException extends Exception {

    public IssuerNotCAException() {
        super("Selected issuer is not a CA");
    }
}
