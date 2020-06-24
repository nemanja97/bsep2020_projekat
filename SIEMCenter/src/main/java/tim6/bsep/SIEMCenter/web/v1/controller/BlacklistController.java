package tim6.bsep.SIEMCenter.web.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tim6.bsep.SIEMCenter.model.Blacklist;
import tim6.bsep.SIEMCenter.service.BlacklistService;
import tim6.bsep.SIEMCenter.web.v1.dto.BlacklistDTO;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/blacklists")
@CrossOrigin()
public class BlacklistController {

    @Autowired
    BlacklistService blacklistService;

    Logger logger = LoggerFactory.getLogger(BlacklistController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getBlacklists(
            Principal principal,
            @RequestParam(value = "id", required = false) Long id) {
        logger.info(String.format("%s called %s method with parameters id=%s", principal.getName(), "getBlacklists", id));
        if (id == null) {
            logger.info(String.format("Method outcome %s", HttpStatus.OK));
            return new ResponseEntity<>(blacklistService.getAll(), HttpStatus.OK);
        }

        Blacklist blacklist = blacklistService.findById(id);
        if (blacklist != null) {
            logger.info(String.format("Method outcome %s", HttpStatus.OK));
            return new ResponseEntity<>(blacklistService.findById(id), HttpStatus.OK);
        }

        logger.warn(String.format("Method outcome %s", HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> createBlacklist(Principal principal, @RequestBody @Valid BlacklistDTO blacklistDTO) {
        logger.info(String.format("%s called %s method with parameters blacklistDTO=%s", principal.getName(), "createBlacklist", blacklistDTO));
        Blacklist blacklist = new Blacklist(blacklistDTO.getId(), blacklistDTO.getName(), blacklistDTO.getContent());
        blacklistService.create(blacklist);
        logger.info(String.format("Method outcome %s", HttpStatus.CREATED));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<?> updateBlacklist(Principal principal, @RequestBody @Valid BlacklistDTO blacklistDTO) {
        logger.info(String.format("%s called %s method with parameters blacklistDTO=%s", principal.getName(), "updateBlacklist", blacklistDTO));
        boolean success = blacklistService.update(blacklistDTO.getId(),
                new Blacklist(blacklistDTO.getId(), blacklistDTO.getName(), blacklistDTO.getContent()));
        if (success) {
            logger.info(String.format("Method outcome %s", HttpStatus.OK));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        logger.warn(String.format("Method outcome %s", HttpStatus.BAD_REQUEST));
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBlacklist(Principal principal, @RequestParam(value = "id", required = false) Long id) {
        logger.info(String.format("%s called %s method with parameters id=%s", principal.getName(), "deleteBlacklist", id));
        boolean success = blacklistService.delete(id);
        if (success) {
            logger.info(String.format("Method outcome %s", HttpStatus.OK));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        logger.warn(String.format("Method outcome %s", HttpStatus.BAD_REQUEST));
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
