package tim6.bsep.pki.mapper;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import tim6.bsep.pki.web.v1.dto.CreateCertificateDTO;

public class CertificateInfoMapper {

    public static X500Name nameFromDTO(CreateCertificateDTO CACertificateDTO){
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, CACertificateDTO.getCommonName());
        builder.addRDN(BCStyle.O, CACertificateDTO.getOrganization());
        builder.addRDN(BCStyle.OU, CACertificateDTO.getOrganizationUnit());
        builder.addRDN(BCStyle.E, CACertificateDTO.getEmail());
        builder.addRDN(BCStyle.C, CACertificateDTO.getCountry());
        return builder.build();
    }

}
