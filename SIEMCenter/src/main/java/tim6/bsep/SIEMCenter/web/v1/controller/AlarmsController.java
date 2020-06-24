package tim6.bsep.SIEMCenter.web.v1.controller;

import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tim6.bsep.SIEMCenter.model.drools.Alarm;
import tim6.bsep.SIEMCenter.pages.AlarmPage;
import tim6.bsep.SIEMCenter.service.AlarmService;
import tim6.bsep.SIEMCenter.web.v1.dto.AlarmListRequest;

import java.security.Principal;

@RestController
@RequestMapping(value = "api/v1/alarms")
@CrossOrigin()
public class AlarmsController {

    @Autowired
    AlarmService alarmService;

    Logger logger = LoggerFactory.getLogger(AlarmsController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<AlarmPage> getAlarms(
            AlarmListRequest request, Pageable pageable, Principal principal) {
        logger.info(String.format("%s called %s method with parameters AlarmListRequest=%s, pageable=%s", principal.getName(), "getAlarms", request, pageable));
        Predicate predicate = alarmService.makeQuery(request);
        Page<Alarm> alarms = alarmService.findPredicate(predicate, pageable);
        AlarmPage alarmPage = new AlarmPage(alarms.getContent(), alarms.getPageable(), alarms.getTotalElements());

        logger.info(String.format("Method outcome %s", HttpStatus.OK));
        return new ResponseEntity<>(alarmPage, HttpStatus.OK);
    }

}
