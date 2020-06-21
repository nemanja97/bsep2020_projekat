package tim6.bsep.SIEMCenter.web.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tim6.bsep.SIEMCenter.model.Blacklist;
import tim6.bsep.SIEMCenter.service.BlacklistService;
import tim6.bsep.SIEMCenter.web.v1.dto.BlacklistDTO;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/blacklists")
@CrossOrigin()
public class BlacklistController {

    @Autowired
    BlacklistService blacklistService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getBlacklists(
            @RequestParam(value = "id", required = false) Long id) {
        if (id == null)
            return new ResponseEntity<List<Blacklist>>(blacklistService.getAll(), HttpStatus.OK);

        Blacklist blacklist = blacklistService.findById(id);
        if (blacklist != null)
            return new ResponseEntity<Blacklist>(blacklistService.findById(id), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> createBlacklist(@RequestBody @Valid BlacklistDTO blacklistDTO) {
        Blacklist blacklist = new Blacklist(blacklistDTO.getId(), blacklistDTO.getName(), blacklistDTO.getContent());
        blacklistService.create(blacklist);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<?> updateBlacklist(@RequestBody @Valid BlacklistDTO blacklistDTO) {
        boolean success = blacklistService.update(blacklistDTO.getId(),
                new Blacklist(blacklistDTO.getId(), blacklistDTO.getName(), blacklistDTO.getContent()));
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBlacklist(@RequestParam(value = "id", required = false) Long id) {
        boolean success = blacklistService.delete(id);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
