package tim6.bsep.SIEMCenter.web.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tim6.bsep.SIEMCenter.model.Whitelist;
import tim6.bsep.SIEMCenter.service.WhitelistService;
import tim6.bsep.SIEMCenter.web.v1.dto.WhitelistDTO;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1/whitelists")
@CrossOrigin()
public class WhitelistController {

    @Autowired
    WhitelistService whitelistService;

    Logger logger = LoggerFactory.getLogger(WhitelistController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getWhitelists(
            Principal principal,
            @RequestParam(value = "id", required = false) Long id) {
        logger.info(String.format("%s called %s method with parameters id=%s", principal.getName(), "getWhitelists", id));
        if (id == null) {
            List<Whitelist> whitelists = whitelistService.getAll();
            List<WhitelistDTO> whitelistDTOS = whitelists.stream().map(WhitelistDTO::new).collect(Collectors.toList());
            logger.info(String.format("Method outcome %s", HttpStatus.OK));
            return new ResponseEntity<>(whitelistDTOS, HttpStatus.OK);
        }

        Whitelist whitelist = whitelistService.findById(id);
        if (whitelist != null) {
            logger.info(String.format("Method outcome %s", HttpStatus.OK));
            return new ResponseEntity<>(new WhitelistDTO(whitelist), HttpStatus.OK);
        }

        logger.warn(String.format("Method outcome %s", HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> createWhitelist(Principal principal, @RequestBody @Valid WhitelistDTO whitelistDTO) {
        logger.info(String.format("%s called %s method with parameters whitelistDTO=%s", principal.getName(), "createWhitelist", whitelistDTO));
        Whitelist whitelist = new Whitelist(whitelistDTO.getId(), whitelistDTO.getName(), whitelistDTO.getContent());
        whitelistService.create(whitelist);
        logger.info(String.format("Method outcome %s", HttpStatus.OK));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<?> updateWhitelist(Principal principal, @RequestBody @Valid WhitelistDTO whitelistDTO) {
        logger.info(String.format("%s called %s method with parameters whitelistDTO=%s", principal.getName(), "updateWhitelist", whitelistDTO));
        boolean success = whitelistService.update(whitelistDTO.getId(),
                new Whitelist(whitelistDTO.getId(), whitelistDTO.getName(), whitelistDTO.getContent()));
        if (success) {
            logger.info(String.format("Method outcome %s", HttpStatus.OK));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        logger.warn(String.format("Method outcome %s", HttpStatus.BAD_REQUEST));
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteWhitelist(Principal principal, @RequestParam(value = "id", required = false) Long id) {
        logger.info(String.format("%s called %s method with parameters id=%s", principal.getName(), "deleteWhitelist", id));
        boolean success = whitelistService.delete(id);
        if (success) {
            logger.info(String.format("Method outcome %s", HttpStatus.OK));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        logger.info(String.format("Method outcome %s", HttpStatus.BAD_REQUEST));
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
