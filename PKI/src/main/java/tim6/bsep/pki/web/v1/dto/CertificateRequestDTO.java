package tim6.bsep.pki.web.v1.dto;


public class CertificateRequestDTO {

    private String CN; // Common name

    private String O; // Business name, organization

    private String OU; // Department name, organizational unit, e.g. HR, Finance, IT

    private String L; // Town, city

    private String ST; // Non abbreviated province, region, country or state name

    private String C; // Two letter ISO-code for country where organization is located

    private String MAIL; // Organization email contact

    public CertificateRequestDTO() {
    }

    public CertificateRequestDTO(String CN, String o, String OU, String l, String ST, String c, String MAIL) {
        this.CN = CN;
        this.O = o;
        this.OU = OU;
        this.L = l;
        this.ST = ST;
        this.C = c;
        this.MAIL = MAIL;
    }

    public String getCN() {
        return CN;
    }

    public void setCN(String CN) {
        this.CN = CN;
    }

    public String getO() {
        return O;
    }

    public void setO(String o) {
        O = o;
    }

    public String getOU() {
        return OU;
    }

    public void setOU(String OU) {
        this.OU = OU;
    }

    public String getL() {
        return L;
    }

    public void setL(String l) {
        L = l;
    }

    public String getST() {
        return ST;
    }

    public void setST(String ST) {
        this.ST = ST;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getMAIL() {
        return MAIL;
    }

    public void setMAIL(String MAIL) {
        this.MAIL = MAIL;
    }
}
