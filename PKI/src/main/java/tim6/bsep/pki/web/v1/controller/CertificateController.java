package tim6.bsep.pki.web.v1.controller;

import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tim6.bsep.pki.exceptions.CertificateNotFoundException;
import tim6.bsep.pki.exceptions.IssuerNotCAException;
import tim6.bsep.pki.mapper.CertificateInfoMapper;
import tim6.bsep.pki.model.RevocationReason;
import tim6.bsep.pki.service.implementation.CertificateServiceImpl;
import tim6.bsep.pki.web.v1.dto.CreateCertificateDTO;

@RestController
@RequestMapping(value = "api/v1/certificates")
public class CertificateController {

    @Autowired
    CertificateServiceImpl certificateServiceImpl;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getValidCertificates() {
        return null;
    }


    public ResponseEntity getRevokedCertificates() {
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getCertificate(@PathVariable String id) {
        return null;
    }

    @RequestMapping(value = "isValid/{id}", method = RequestMethod.GET)
    public Boolean isValid(@PathVariable String id) {
        return certificateServiceImpl.isCertificateValid(id);
    }

    @RequestMapping(value = "/ca/{id}", method = RequestMethod.GET)
    public ResponseEntity getCertificatesOfCA(@PathVariable String id) {
        return null;
    }

    @RequestMapping(value = "/ca", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity createCACertificate(@RequestBody CreateCertificateDTO CACertificateDTO) throws CertificateNotFoundException, IssuerNotCAException {
        X500Name subjectData = CertificateInfoMapper.nameFromDTO(CACertificateDTO);
        certificateServiceImpl.createCertificate(CACertificateDTO.getIssuerAlias(), subjectData,true);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/leaf", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity createLeafCertificate(@RequestBody CreateCertificateDTO CertificateDTO) throws CertificateNotFoundException, IssuerNotCAException {
        X500Name subjectData = CertificateInfoMapper.nameFromDTO(CertificateDTO);
        certificateServiceImpl.createCertificate(CertificateDTO.getIssuerAlias(), subjectData,false);
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
