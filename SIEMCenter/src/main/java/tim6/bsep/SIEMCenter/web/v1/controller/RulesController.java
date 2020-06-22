package tim6.bsep.SIEMCenter.web.v1.controller;

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tim6.bsep.SIEMCenter.exceptions.RuleNotCompilingException;
import tim6.bsep.SIEMCenter.model.Rule;
import tim6.bsep.SIEMCenter.service.RuleService;
import tim6.bsep.SIEMCenter.web.v1.dto.RuleDTO;
import tim6.bsep.SIEMCenter.web.v1.dto.RuleListRequest;
import tim6.bsep.SIEMCenter.web.v1.predicate.RulePredicate;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/v1/rules")
@CrossOrigin()
public class RulesController {

    @Autowired
    RuleService ruleService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getRules(RuleListRequest ruleListRequest, Pageable pageable) {
        Predicate predicate = new RulePredicate().makeQuery(ruleListRequest);
        Page<Rule> rules = ruleService.findPredicate(predicate, pageable);
        return new ResponseEntity<>(rules, HttpStatus.OK);
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

    @RequestMapping(value="/validate", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> validateRule(@RequestBody @Valid RuleDTO ruleDTO) {
        Rule rule = new Rule(ruleDTO.getId(), ruleDTO.getName(), ruleDTO.getContent(), ruleDTO.getProduces(), ruleDTO.getConsumes());
        try {
            ruleService.validate(rule);
        } catch (RuleNotCompilingException e) {
            return new ResponseEntity<>(e.getReason(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<?> updateRule(@RequestBody @Valid RuleDTO ruleDTO) {
        try {
            boolean success = ruleService.update(ruleDTO.getId(),
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
        if (ruleService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
