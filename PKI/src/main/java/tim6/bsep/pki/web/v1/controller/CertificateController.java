package tim6.bsep.pki.web.v1.controller;

import com.google.gson.GsonBuilder;
import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import tim6.bsep.pki.exceptions.CertificateNotFoundException;
import tim6.bsep.pki.exceptions.IssuerNotCAException;
import tim6.bsep.pki.exceptions.IssuerNotValidException;
import tim6.bsep.pki.exceptions.UnknownTemplateException;
import tim6.bsep.pki.mapper.CertificateInfoMapper;
import tim6.bsep.pki.model.CertificateInfo;
import tim6.bsep.pki.model.CertificateInfoWithChildren;
import tim6.bsep.pki.model.RevocationReason;
import tim6.bsep.pki.service.CertificateInfoService;
import tim6.bsep.pki.service.implementation.CertificateServiceImpl;
import tim6.bsep.pki.web.v1.dto.CertificateDTO;
import tim6.bsep.pki.web.v1.dto.CreateCertificateDTO;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Map;

@RestController
@RequestMapping(value = "api/v1/certificates")
@CrossOrigin()
public class CertificateController {

    @Autowired
    CertificateServiceImpl certificateServiceImpl;

    @Autowired
    CertificateInfoService certificateInfoService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getCertificates(
            @RequestParam(value = "onlyValid", required = false) Boolean onlyValid
    ) {
        Map<CertificateInfo, CertificateInfoWithChildren> nodeMap = certificateInfoService.findAll(onlyValid != null && onlyValid);
        CertificateInfo root = certificateInfoService.findById(1L);

        String json = new GsonBuilder().setPrettyPrinting()
                .create()
                .toJson(nodeMap.get(root));

        return new ResponseEntity(json, HttpStatus.OK);
    }

    @RequestMapping(value = "/{alias}", method = RequestMethod.GET)
    public ResponseEntity getCertificate(
            @PathVariable String alias,
            @RequestParam(value = "format", required = false, defaultValue = "text") String format) throws CertificateEncodingException, IOException, CertificateParsingException {
        X509Certificate certificate = certificateServiceImpl.findByAlias(alias);

        switch (format) {
            case "pem":
                String pemCertificate = certificateServiceImpl.writeCertificateToPEM(certificate);

                byte[] contents = pemCertificate.getBytes();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                String filename = "certificate.pem";
                ContentDisposition contentDisposition = ContentDisposition
                        .builder("inline")
                        .filename(filename)
                        .build();
                headers.setContentDisposition(contentDisposition);
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                return new ResponseEntity<>(contents, headers, HttpStatus.OK);
            default:
                CertificateDTO certificateDTO = CertificateInfoMapper.certificateDTOFromCertificate(certificate);
                return new ResponseEntity(certificateDTO, HttpStatus.OK);
        }

    }

    @RequestMapping(value = "isValid/{id}", method = RequestMethod.GET)
    public Boolean isValid(@PathVariable String id) {
        return certificateServiceImpl.isCertificateValid(id);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity createCertificate(@RequestBody CreateCertificateDTO certificateDTO) throws CertificateNotFoundException, IssuerNotCAException, IssuerNotValidException, UnknownTemplateException {
        X500Name subjectData = CertificateInfoMapper.nameFromDTO(certificateDTO);
        certificateServiceImpl.createCertificate(certificateDTO.getIssuerAlias(), certificateDTO.getAlias(), subjectData, certificateDTO.getTemplate());
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(name = "/validate", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity validateCertificate() {
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity revokeCertificate(@PathVariable Long id, @RequestParam RevocationReason reason) {
        boolean success = certificateServiceImpl.revokeCertificate(id, reason);
        if (success)
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }

}
