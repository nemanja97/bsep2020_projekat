package tim6.bsep.pki.exceptions;

public class ExpirableLinkNotPresentException extends Exception {

    public ExpirableLinkNotPresentException() {
        super("ExpirableLink doesn't exist");
    }
}
