package tim6.bsep.pki.web.v1.controller;

import com.google.gson.GsonBuilder;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.keycloak.authorization.client.util.Http;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import tim6.bsep.pki.utility.SignatureUtility;
import tim6.bsep.pki.web.v1.dto.CertificateDTO;
import tim6.bsep.pki.web.v1.dto.CreateCertificateDTO;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Date;
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

    @Autowired
    SignatureUtility signatureUtility;

    Logger logger = LoggerFactory.getLogger(CertificateController.class);


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getCertificates(
            Principal principal,
            @RequestParam(value = "onlyValid", required = false) Boolean onlyValid
    ) {
        logger.info(String.format("%s called %s method with parameters onlyValid=%s", principal.getName(), "getCertificates", onlyValid));
        Map<CertificateInfo, CertificateInfoWithChildren> nodeMap = certificateInfoService.findAll(onlyValid != null && onlyValid);
        CertificateInfo root = certificateInfoService.findById(1L);

        String json = new GsonBuilder().setPrettyPrinting()
                .create()
                .toJson(nodeMap.get(root));

        logger.info(String.format("Method outcome %s", HttpStatus.OK));
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @RequestMapping(value = "/{alias}", method = RequestMethod.GET)
    public ResponseEntity<Object> getCertificate(
            Principal principal,
            @PathVariable String alias,
            @RequestParam(value = "format", required = false, defaultValue = "text") String format) throws IOException, CertificateParsingException {
        logger.info(String.format("%s called %s method with parameters alias=%s, format=%s", principal.getName(), "getCertificate", alias, format));
        X509Certificate certificate = certificateServiceImpl.findByAlias(alias);
        if (certificate == null) {
            logger.info(String.format("Method outcome %s", HttpStatus.NOT_FOUND));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

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
                logger.info(String.format("Method outcome %s", HttpStatus.OK));
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
                logger.info(String.format("Method outcome %s", HttpStatus.OK));
                return new ResponseEntity<>(contents, headers, HttpStatus.OK);
            }
            default:
                CertificateInfo dbCert = certificateInfoService.findByAlias(alias);
                CertificateDTO certificateDTO = CertificateInfoMapper.certificateDTOFromCertificate(certificate);
                certificateDTO.setId(dbCert.getId());
                certificateDTO.setAlias(alias);
                logger.info(String.format("Method outcome %s", HttpStatus.OK));
                return new ResponseEntity<>(certificateDTO, HttpStatus.OK);
        }

    }

    @RequestMapping(value = "isValid", method = RequestMethod.POST)
    public ResponseEntity isValid(@RequestBody byte[] msg) throws CMSException, OperatorCreationException, IOException, CertificateEncodingException {
        if (certificateServiceImpl.isCertificateValid("SIEMCenter"))
            if (signatureUtility.verifySignature(msg, "SIEMCenter")) {
                String alias = signatureUtility.extractMessage(msg);
                boolean isValid = certificateServiceImpl.isCertificateValid(alias);
                byte[] signedMsg = signatureUtility.signMessage(String.valueOf(isValid));
                return new ResponseEntity(signedMsg, HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        else
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Object> createCertificate(Principal principal, @Valid @RequestBody CreateCertificateDTO certificateDTO) throws CertificateNotFoundException, IssuerNotCAException, IssuerNotValidException, UnknownTemplateException, AliasAlreadyTakenException {
        logger.info(String.format("%s called %s method with parameters certificateDTO=%s", principal.getName(), "createCertificate", certificateDTO));
        X500Name subjectData = CertificateInfoMapper.nameFromDTO(certificateDTO);
        certificateServiceImpl.createCertificate(certificateDTO.getIssuerAlias(), certificateDTO.getAlias(), subjectData, certificateDTO.getTemplate());

        logger.info(String.format("Method outcome %s", HttpStatus.OK));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> revokeCertificate(Principal principal, @PathVariable Long id, @RequestParam RevocationReason reason) {
        logger.info(String.format("%s called %s method with parameters id=%s revocationReason=%s", principal.getName(), "revokeCertificate", id, reason));
        boolean success = certificateServiceImpl.revokeCertificate(id, reason);
        if (success) {
            logger.info(String.format("Method outcome %s", HttpStatus.OK));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            logger.warn(String.format("Method outcome %s", HttpStatus.NOT_MODIFIED));
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

}
