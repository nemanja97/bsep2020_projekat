package tim6.bsep.pki.exceptions;

public class UnknownTemplateException extends Exception {

    public UnknownTemplateException(String message) {
        super("Unknown template: " + message);
    }
}
