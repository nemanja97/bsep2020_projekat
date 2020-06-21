package tim6.bsep.SIEMCenter.web.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tim6.bsep.SIEMCenter.model.Whitelist;
import tim6.bsep.SIEMCenter.service.WhitelistService;
import tim6.bsep.SIEMCenter.web.v1.dto.WhitelistDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1/whitelists")
@CrossOrigin()
public class WhitelistController {

    @Autowired
    WhitelistService whitelistService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getWhitelists(
            @RequestParam(value = "id", required = false) Long id) {
        if (id == null) {
            List<Whitelist> whitelists = whitelistService.getAll();
            List<WhitelistDTO> whitelistDTOS = whitelists.stream().map(WhitelistDTO::new).collect(Collectors.toList());
            return new ResponseEntity<>(whitelistDTOS, HttpStatus.OK);
        }

        Whitelist whitelist = whitelistService.findById(id);
        if (whitelist != null)
            return new ResponseEntity<>(new WhitelistDTO(whitelist), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> createWhitelist(@RequestBody @Valid WhitelistDTO whitelistDTO) {
        Whitelist whitelist = new Whitelist(whitelistDTO.getId(), whitelistDTO.getName(), whitelistDTO.getContent());
        whitelistService.create(whitelist);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<?> updateWhitelist(@RequestBody @Valid WhitelistDTO whitelistDTO) {
        boolean success = whitelistService.update(whitelistDTO.getId(),
                new Whitelist(whitelistDTO.getId(), whitelistDTO.getName(), whitelistDTO.getContent()));
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteWhitelist(@RequestParam(value = "id", required = false) Long id) {
        boolean success = whitelistService.delete(id);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
