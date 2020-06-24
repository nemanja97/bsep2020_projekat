package tim6.bsep.SIEMCenter.web.v1.controller;

import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.security.Principal;

@RestController
@RequestMapping(value = "api/v1/rules")
@CrossOrigin()
public class RulesController {

    @Autowired
    RuleService ruleService;

    Logger logger = LoggerFactory.getLogger(RulesController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getRules(Principal principal, RuleListRequest ruleListRequest, Pageable pageable) {
        logger.info(String.format("%s called %s method with parameters ruleListRequest=%s, pageable=%s", principal.getName(), "getRules", ruleListRequest, pageable));
        Predicate predicate = new RulePredicate().makeQuery(ruleListRequest);
        Page<Rule> rules = ruleService.findPredicate(predicate, pageable);
        logger.info(String.format("Method outcome %s", HttpStatus.OK));
        return new ResponseEntity<>(rules, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> createRule(Principal principal, @RequestBody @Valid RuleDTO ruleDTO) {
        logger.info(String.format("%s called %s method with parameters ruleDTO=%s", principal.getName(), "createRule", ruleDTO));
        Rule rule = new Rule(ruleDTO.getId(), ruleDTO.getName(), ruleDTO.getContent(), ruleDTO.getProduces(), ruleDTO.getConsumes());
        try {
            ruleService.create(rule);
        } catch (RuleNotCompilingException e) {
            logger.warn(String.format("Method outcome %s", HttpStatus.BAD_REQUEST));
            return new ResponseEntity<>(e.getReason(), HttpStatus.BAD_REQUEST);
        }
        logger.info(String.format("Method outcome %s", HttpStatus.CREATED));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value="/validate", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> validateRule(Principal principal, @RequestBody @Valid RuleDTO ruleDTO) {
        logger.info(String.format("%s called %s method with parameters ruleDTO=%s", principal.getName(), "validateRule", ruleDTO));
        Rule rule = new Rule(ruleDTO.getId(), ruleDTO.getName(), ruleDTO.getContent(), ruleDTO.getProduces(), ruleDTO.getConsumes());
        try {
            ruleService.validate(rule);
        } catch (RuleNotCompilingException e) {
            logger.warn(String.format("Method outcome %s", HttpStatus.BAD_REQUEST));
            return new ResponseEntity<>(e.getReason(), HttpStatus.BAD_REQUEST);
        }
        logger.info(String.format("Method outcome %s", HttpStatus.CREATED));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<?> updateRule(Principal principal, @RequestBody @Valid RuleDTO ruleDTO) {
        logger.info(String.format("%s called %s method with parameters ruleDTO=%s", principal.getName(), "updateRule", ruleDTO));
        try {
            boolean success = ruleService.update(ruleDTO.getId(),
                    new Rule(ruleDTO.getId(), ruleDTO.getName(), ruleDTO.getContent(), ruleDTO.getProduces(), ruleDTO.getConsumes()));
            if (success) {
                logger.info(String.format("Method outcome %s", HttpStatus.OK));
                return new ResponseEntity<>(HttpStatus.OK);
            }
            logger.warn(String.format("Method outcome %s", HttpStatus.BAD_REQUEST));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuleNotCompilingException e) {
            logger.warn(String.format("Method outcome %s", HttpStatus.BAD_REQUEST));
            return new ResponseEntity<>(e.getReason(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRule(Principal principal, @RequestParam(value = "id", required = false) Long id) {
        logger.info(String.format("%s called %s method with parameters id=%s", principal.getName(), "deleteRule", id));
        if (ruleService.delete(id)) {
            logger.info(String.format("Method outcome %s", HttpStatus.OK));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        logger.warn(String.format("Method outcome %s", HttpStatus.BAD_REQUEST));
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
