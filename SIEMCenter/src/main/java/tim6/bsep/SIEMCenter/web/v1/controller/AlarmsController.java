package tim6.bsep.SIEMCenter.web.v1.controller;

import org.bouncycastle.cms.CMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.*;
import tim6.bsep.SIEMCenter.mapper.LogMapper;
import tim6.bsep.SIEMCenter.service.LogService;
import tim6.bsep.SIEMCenter.utility.SignatureUtility;
import tim6.bsep.SIEMCenter.web.v1.dto.LogDTO;

import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.ScheduledFuture;

@RestController
@RequestMapping(value = "api/v1/alarms")
@CrossOrigin()
public class AlarmsController {


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> test() {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
