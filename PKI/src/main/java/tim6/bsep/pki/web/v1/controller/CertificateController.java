package tim6.bsep.pki.web.v1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/certificates")
public class CertificateController {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getValidCertificates() {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getRevokedCertificates() {
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getCertificate(@PathVariable String id) {
        return null;
    }

    @RequestMapping(value = "/ca/{id}", method = RequestMethod.GET)
    public ResponseEntity getCertificatesOfCA(@PathVariable String id) {
        return null;
    }

    @RequestMapping(value = "/ca", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity createCACertificate() {
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity createLeafCertificate() {
        return null;
    }

    @RequestMapping(name = "/validate", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity validateCertificate() {
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = "application/json")
    public ResponseEntity revokeCertificate(@PathVariable String id) {
        return null;
    }

}
