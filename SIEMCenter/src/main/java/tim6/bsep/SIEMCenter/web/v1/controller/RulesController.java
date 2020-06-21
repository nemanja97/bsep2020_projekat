package tim6.bsep.SIEMCenter.web.v1.controller;

import org.kie.api.builder.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tim6.bsep.SIEMCenter.exceptions.RuleNotCompilingException;
import tim6.bsep.SIEMCenter.model.Rule;
import tim6.bsep.SIEMCenter.service.RuleService;
import tim6.bsep.SIEMCenter.web.v1.dto.RuleDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1/rules")
@CrossOrigin()
public class RulesController {

    @Autowired
    RuleService ruleService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getRules(
            @RequestParam(value = "id", required = false) Long id) {
        if (id == null) {
            List<Rule> rules = ruleService.getAll();
            List<RuleDTO> ruleDTOS = rules.stream().map(RuleDTO::new).collect(Collectors.toList());
            return new ResponseEntity<>(ruleDTOS, HttpStatus.OK);
        }

        Rule rule = ruleService.findById(id);
        if (rule != null)
            return new ResponseEntity<>(new RuleDTO(rule), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> createRule(@RequestBody @Valid RuleDTO ruleDTO) {
        Rule rule = new Rule(ruleDTO.getId(), ruleDTO.getName(), ruleDTO.getContent(), ruleDTO.getProduces(), ruleDTO.getConsumes());
        try {
            ruleService.create(rule);
        } catch (RuleNotCompilingException e) {
            return new ResponseEntity<>(e.getReason(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<?> updateRule(@RequestBody @Valid RuleDTO ruleDTO) {
        boolean success = false;
        try {
            success = ruleService.update(ruleDTO.getId(),
                    new Rule(ruleDTO.getId(), ruleDTO.getName(), ruleDTO.getContent(), ruleDTO.getProduces(), ruleDTO.getConsumes()));
            if (success) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuleNotCompilingException e) {
            return new ResponseEntity<>(e.getReason(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRule(@RequestParam(value = "id", required = false) Long id) {
        boolean success = ruleService.delete(id);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
