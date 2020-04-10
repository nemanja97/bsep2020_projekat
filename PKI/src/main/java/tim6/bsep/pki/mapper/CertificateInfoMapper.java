package tim6.bsep.pki.mapper;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.Extension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tim6.bsep.pki.exceptions.IssuerNotCAException;
import tim6.bsep.pki.web.v1.dto.CertificateDTO;
import tim6.bsep.pki.web.v1.dto.CreateCertificateDTO;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;

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

    public static CertificateDTO certificateDTOFromCertificate(X509Certificate certificate) throws IOException, CertificateParsingException {
        CertificateDTO certificateDTO = new CertificateDTO();
        CertificateDTO issuerDTO = new CertificateDTO();

        X500Name subjectName = new X500Name(certificate.getSubjectX500Principal().getName());
        X500Name issuerName = new X500Name(certificate.getIssuerX500Principal().getName());

        RDN cn = subjectName.getRDNs(BCStyle.CN)[0];
        RDN o = subjectName.getRDNs(BCStyle.O)[0];
        RDN ou = subjectName.getRDNs(BCStyle.OU)[0];
        RDN e = subjectName.getRDNs(BCStyle.E)[0];
        RDN c = subjectName.getRDNs(BCStyle.C)[0];

        RDN issuer_cn = issuerName.getRDNs(BCStyle.CN)[0];
        RDN issuer_o = issuerName.getRDNs(BCStyle.O)[0];
        RDN issuer_ou = issuerName.getRDNs(BCStyle.OU)[0];
        RDN issuer_e = issuerName.getRDNs(BCStyle.E)[0];
        RDN issuer_c = issuerName.getRDNs(BCStyle.C)[0];

        certificateDTO.setCommonName(IETFUtils.valueToString(cn.getFirst().getValue()));
        certificateDTO.setOrganization(IETFUtils.valueToString(o.getFirst().getValue()));
        certificateDTO.setOrganizationUnit(IETFUtils.valueToString(ou.getFirst().getValue()));
        certificateDTO.setEmail(IETFUtils.valueToString(e.getFirst().getValue()));
        certificateDTO.setCountry(IETFUtils.valueToString(c.getFirst().getValue()));

        certificateDTO.setIssuer_commonName(IETFUtils.valueToString(issuer_cn.getFirst().getValue()));
        certificateDTO.setIssuer_organization(IETFUtils.valueToString(issuer_o.getFirst().getValue()));
        certificateDTO.setIssuer_organizationUnit(IETFUtils.valueToString(issuer_ou.getFirst().getValue()));
        certificateDTO.setIssuer_email(IETFUtils.valueToString(issuer_e.getFirst().getValue()));
        certificateDTO.setIssuer_country(IETFUtils.valueToString(issuer_c.getFirst().getValue()));

        if (certificate.getBasicConstraints() == -1) {
            certificateDTO.setCa(false);
        } else {
            certificateDTO.setCa(true);
        }

        // https://docs.oracle.com/javase/7/docs/api/java/security/cert/X509Extension.html
        HashMap<String, String> extensions = new HashMap<>();

        String subjectKeyIdentifier = getExtensionValue(certificate, "2.5.29.14");
        if (subjectKeyIdentifier != null)
            extensions.put("SubjectKeyIdentifier", subjectKeyIdentifier);

        String keyUsage = getExtensionValue(certificate, "2.5.29.15");
        if (keyUsage != null)
            extensions.put("KeyUsage", subjectKeyIdentifier);

        String privateKeyUsage = getExtensionValue(certificate, "2.5.29.16");
        if (privateKeyUsage != null)
            extensions.put("PrivateKeyUsage", privateKeyUsage);

        String subjectAlternativeName = getExtensionValue(certificate, "2.5.29.17");
        if (subjectAlternativeName != null)
            extensions.put("SubjectAlternativeName", subjectAlternativeName);

        String issuerAlternativeName = getExtensionValue(certificate, "2.5.29.18");
        if (issuerAlternativeName != null)
            extensions.put("IssuerAlternativeName", issuerAlternativeName);

        String basicConstraints = getExtensionValue(certificate, "2.5.29.19");
        if (basicConstraints != null)
            extensions.put("BasicConstraints", basicConstraints);

        String nameConstraints = getExtensionValue(certificate, "2.5.29.30");
        if (nameConstraints != null)
            extensions.put("NameConstraints", nameConstraints);

        String policyMappings = getExtensionValue(certificate, "2.5.29.33");
        if (policyMappings != null)
            extensions.put("PolicyMappings", policyMappings);

        String authorityKeyIdentifier = getExtensionValue(certificate, "2.5.29.35");
        if (authorityKeyIdentifier != null)
            extensions.put("AuthorityKeyIdentifier", authorityKeyIdentifier);

        String policyConstraints = getExtensionValue(certificate, "2.5.29.36");
        if (policyConstraints != null)
            extensions.put("PolicyConstraints", policyConstraints);

        certificateDTO.setExtensions(extensions);
        return certificateDTO;
    }

    public static String getExtensionValue(X509Certificate certificate, String oid) throws IOException {
        byte[] uid = certificate.getExtensionValue(oid);
        if (uid == null) {
            return null;
        }

        ASN1Primitive derObject = toDERObject(uid);
        DEROctetString derOctetString = (DEROctetString)derObject;
        derObject = toDERObject(derOctetString.getOctets());

        return derObject.toString();
    }

    public static ASN1Primitive toDERObject(byte[] data) throws IOException
    {
        ByteArrayInputStream inStream = new ByteArrayInputStream(data);
        ASN1InputStream DIS = new ASN1InputStream(inStream);
        return DIS.readObject();
    }

}
