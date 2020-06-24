package tim6.bsep.SIEMCenter.web.v1.controller;

import com.querydsl.core.types.Predicate;
import org.bouncycastle.cms.CMSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tim6.bsep.SIEMCenter.mapper.LogMapper;
import tim6.bsep.SIEMCenter.service.AlarmService;
import tim6.bsep.SIEMCenter.model.Log;
import tim6.bsep.SIEMCenter.service.LogService;
import tim6.bsep.SIEMCenter.utility.SignatureUtility;
import tim6.bsep.SIEMCenter.web.v1.dto.LogDTO;
import tim6.bsep.SIEMCenter.web.v1.dto.LogListRequest;
import tim6.bsep.SIEMCenter.web.v1.predicate.LogPredicate;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping(value = "api/v1/logs")
@CrossOrigin()
public class LogsController {

    @Autowired
    LogService logService;

    @Autowired
    AlarmService alarmService;

    Logger logger = LoggerFactory.getLogger(LogsController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> findLogs(Principal principal, LogListRequest logListRequest, Pageable pageable){
        logger.info(String.format("%s called %s method with parameters logListRequest=%s", principal.getName(), "findLogs", logListRequest));
        Predicate predicate = new LogPredicate().makeQuery(logListRequest);
        Page<Log> logs = logService.findPredicate(predicate, pageable);
        logger.info(String.format("Method outcome %s", HttpStatus.OK));
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

    @RequestMapping(value = "/receive", method = RequestMethod.POST, consumes = "application/octet-stream")
    public ResponseEntity<Object> receiveLog(@Valid @RequestBody byte[] signedLog) throws IOException, CMSException {
        if(SignatureUtility.checkMessage(signedLog)){
            LogDTO logDTO = SignatureUtility.extractMessage(signedLog);
            logService.save(LogMapper.LogFromDTO(logDTO));
            alarmService.saveNewAlarmsFromSession();
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
