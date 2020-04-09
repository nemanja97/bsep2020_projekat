package tim6.bsep.pki.model;

public enum RevocationReason {

    unspecified(0),
    keyCompromise(1),
    cACompromise(2),
    affiliationChanged(3),
    superseded(4),
    cessationOfOperation(5),
    certificateHold(6),
    removeFromCRL(8),
    privilegeWithdrawn(9),
    aACompromise(10);

    private final int id;

    RevocationReason(int id) {
        this.id = id;
    }

    public int getValue() { return id; }

}
