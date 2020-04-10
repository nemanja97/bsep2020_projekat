package tim6.bsep.pki.exceptions;

public class IssuerNotValidException extends Exception {

    public IssuerNotValidException() {
        super("Issuer is not valid");
    }
}
