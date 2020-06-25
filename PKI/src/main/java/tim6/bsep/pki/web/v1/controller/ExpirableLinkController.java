package tim6.bsep.pki.web.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tim6.bsep.pki.service.ExpirableLinkService;

import java.security.Principal;

@RestController
@RequestMapping(value = "api/v1/expirableLinks")
@CrossOrigin()
public class ExpirableLinkController {

    Logger logger = LoggerFactory.getLogger(CertificateController.class);


    @Autowired
    ExpirableLinkService expirableLinkService;

    @RequestMapping(value = "/isValid", method = RequestMethod.GET)
    public ResponseEntity<String> checkIfLinkIsValid(
            @RequestParam(value = "link") String link
    ) {
        boolean isValid = expirableLinkService.isExpirableLinkUsed(link);
        if (isValid)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAll(
            Principal principal
    ) {
        logger.info(String.format("%s called %s method", principal.getName(), "getAll"));
        return new ResponseEntity<>(expirableLinkService.getExpirableLinks(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> createExpirableLink(
            Principal principal
    ) {
        logger.info(String.format("%s called %s method", principal.getName(), "createExpirableLink"));
        expirableLinkService.createExpirableLink();
        logger.info(String.format("Method outcome %s", HttpStatus.OK));
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
