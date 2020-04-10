package tim6.bsep.pki.exceptions;

public class AliasAlreadyTakenException extends Exception {

    public AliasAlreadyTakenException() {
        super("Alias already taken");
    }
}
