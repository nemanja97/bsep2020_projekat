package tim6.bsep.SIEMCenter.web.v1.controller;

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tim6.bsep.SIEMCenter.model.drools.Alarm;
import tim6.bsep.SIEMCenter.model.enums.FacilityType;
import tim6.bsep.SIEMCenter.model.enums.LogType;
import tim6.bsep.SIEMCenter.model.enums.SeverityLevel;
import tim6.bsep.SIEMCenter.pages.AlarmPage;
import tim6.bsep.SIEMCenter.service.AlarmService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/alarms")
@CrossOrigin()
public class AlarmsController {

    @Autowired
    AlarmService alarmService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<AlarmPage> getAlarms(
            @RequestParam(value = "ids", required = false) List<Long> ids,
            @RequestParam(value = "logIds", required = false) List<Long> logIds,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date fromDate,
            @RequestParam(value = "toDate",  required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date toDate,
            @RequestParam(value = "facilityTypes", required = false) List<FacilityType> facilityTypes,
            @RequestParam(value = "severityLevels", required = false) List<SeverityLevel> severityLevels,
            @RequestParam(value = "hostnames", required = false) List<String> hostnames,
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "type", required = false) LogType logType,
            Pageable pageable
            ) {
        Predicate predicate = alarmService.makeQuery(ids, logIds, fromDate, toDate, facilityTypes, severityLevels, hostnames, message, logType);
        Page<Alarm> alarms = alarmService.findPredicate(predicate, pageable);
        AlarmPage alarmPage = new AlarmPage(alarms.getContent(), alarms.getPageable(), alarms.getTotalElements());

        return new ResponseEntity<>(alarmPage, HttpStatus.OK);
    }

}
