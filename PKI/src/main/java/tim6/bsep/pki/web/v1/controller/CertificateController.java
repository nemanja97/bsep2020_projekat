package tim6.bsep.pki.web.v1.controller;

import com.google.gson.GsonBuilder;
import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import tim6.bsep.pki.exceptions.*;
import tim6.bsep.pki.mapper.CertificateInfoMapper;
import tim6.bsep.pki.model.CertificateInfo;
import tim6.bsep.pki.model.CertificateInfoWithChildren;
import tim6.bsep.pki.model.RevocationReason;
import tim6.bsep.pki.service.CertificateInfoService;
import tim6.bsep.pki.service.KeyStoreService;
import tim6.bsep.pki.service.implementation.CertificateServiceImpl;
import tim6.bsep.pki.web.v1.dto.CertificateDTO;
import tim6.bsep.pki.web.v1.dto.CreateCertificateDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    @Autowired
    KeyStoreService keyStoreService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getCertificates(
            @RequestParam(value = "onlyValid", required = false) Boolean onlyValid
    ) {
        Map<CertificateInfo, CertificateInfoWithChildren> nodeMap = certificateInfoService.findAll(onlyValid != null && onlyValid);
        CertificateInfo root = certificateInfoService.findById(1L);

        String json = new GsonBuilder().setPrettyPrinting()
                .create()
                .toJson(nodeMap.get(root));

        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @RequestMapping(value = "/{alias}", method = RequestMethod.GET)
    public ResponseEntity<Object> getCertificate(
            @PathVariable String alias,
            @RequestParam(value = "format", required = false, defaultValue = "text") String format) throws IOException, CertificateParsingException {
        X509Certificate certificate = certificateServiceImpl.findByAlias(alias);
        if (certificate == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        switch (format) {
            case "pem_key": {
                ByteArrayOutputStream outputStream = certificateServiceImpl.getPemCertificateChainWithPrivateKey(alias);
                byte[] contents = outputStream.toByteArray();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                String filename = "certificate.zip";
                ContentDisposition contentDisposition = ContentDisposition
                        .builder("inline")
                        .filename(filename)
                        .build();
                headers.setContentDisposition(contentDisposition);
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                return new ResponseEntity<>(contents, headers, HttpStatus.OK);
            }
            case "pem": {
                String certificateChain = certificateServiceImpl.getPemCertificateChain(alias);
                byte[] contents = certificateChain.getBytes();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                String filename = "certificate.crt";
                ContentDisposition contentDisposition = ContentDisposition
                        .builder("inline")
                        .filename(filename)
                        .build();
                headers.setContentDisposition(contentDisposition);
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                return new ResponseEntity<>(contents, headers, HttpStatus.OK);
            }
            default:
                CertificateInfo dbCert = certificateInfoService.findByAlias(alias);
                CertificateDTO certificateDTO = CertificateInfoMapper.certificateDTOFromCertificate(certificate);
                certificateDTO.setId(dbCert.getId());
                certificateDTO.setAlias(alias);
                return new ResponseEntity<>(certificateDTO, HttpStatus.OK);
        }

    }

    @RequestMapping(value = "isValid/{id}", method = RequestMethod.GET)
    public Boolean isValid(@PathVariable String id) {
        return certificateServiceImpl.isCertificateValid(id);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Object> createCertificate(@RequestBody CreateCertificateDTO certificateDTO) throws CertificateNotFoundException, IssuerNotCAException, IssuerNotValidException, UnknownTemplateException, AliasAlreadyTakenException {
        X500Name subjectData = CertificateInfoMapper.nameFromDTO(certificateDTO);
        certificateServiceImpl.createCertificate(certificateDTO.getIssuerAlias(), certificateDTO.getAlias(), subjectData, certificateDTO.getTemplate());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> revokeCertificate(@PathVariable Long id, @RequestParam RevocationReason reason) {
        boolean success = certificateServiceImpl.revokeCertificate(id, reason);
        if (success)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

}
