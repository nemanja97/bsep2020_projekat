package tim6.bsep.SIEMCenter.web.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tim6.bsep.SIEMCenter.service.LogService;
import tim6.bsep.SIEMCenter.web.v1.dto.ReportData;
import tim6.bsep.SIEMCenter.service.AlarmService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value="api/v1/reports")
@CrossOrigin()
public class ReportsController {

    @Autowired
    AlarmService alarmService;

    @Autowired
    LogService logService;

    @RequestMapping(value="/alarms/monthly/type", method = RequestMethod.GET)
    public ResponseEntity<List<ReportData>> getAlarmsMonthlyCountByType(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<String> datesString = handleDates(startDate, endDate);
        List<ReportData> retVal = alarmService.getAlarmsMonthlyCount(datesString.get(0), datesString.get(1));
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @RequestMapping(value="/alarms/weekly/type", method = RequestMethod.GET)
    public ResponseEntity<List<ReportData>> getAlarmsWeeklyCountByType(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<String> datesString = handleDates(startDate, endDate);
        List<ReportData> retVal = alarmService.getAlarmsWeeklyCount(datesString.get(0), datesString.get(1));
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @RequestMapping(value="/alarms/daily/type", method = RequestMethod.GET)
    public ResponseEntity<List<ReportData>> getAlarmsDailyCountByType(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<String> datesString = handleDates(startDate, endDate);
        List<ReportData> retVal = alarmService.getAlarmsDailyCount(datesString.get(0), datesString.get(1));
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @RequestMapping(value="/alarms/total/type", method = RequestMethod.GET)
    public ResponseEntity<List<ReportData>> getAlarmsTotalCountByType(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<String> datesString = handleDates(startDate, endDate);
        List<ReportData> retVal = alarmService.getAlarmsTotalCountByType(datesString.get(0), datesString.get(1));
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @RequestMapping(value="/alarms/total/hostname", method = RequestMethod.GET)
    public ResponseEntity<List<ReportData>> getAlarmsTotalCountByHostname(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<String> datesString = handleDates(startDate, endDate);
        List<ReportData> retVal = alarmService.getAlarmsTotalCountByHostname(datesString.get(0), datesString.get(1));
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @RequestMapping(value="/logs/monthly/type", method = RequestMethod.GET)
    public ResponseEntity<List<ReportData>> getLogsMonthlyCountByType(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<String> datesString = handleDates(startDate, endDate);
        List<ReportData> retVal = logService.getLogsMonthlyCount(datesString.get(0), datesString.get(1));
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @RequestMapping(value="/logs/weekly/type", method = RequestMethod.GET)
    public ResponseEntity<List<ReportData>> getLogsWeeklyCountByType(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<String> datesString = handleDates(startDate, endDate);
        List<ReportData> retVal = logService.getLogsWeeklyCount(datesString.get(0), datesString.get(1));
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @RequestMapping(value="/logs/daily/type", method = RequestMethod.GET)
    public ResponseEntity<List<ReportData>> getLogsDailyCountByType(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<String> datesString = handleDates(startDate, endDate);
        List<ReportData> retVal = logService.getLogsDailyCount(datesString.get(0), datesString.get(1));
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @RequestMapping(value="/logs/total/type", method = RequestMethod.GET)
    public ResponseEntity<List<ReportData>> getLogsTotalCountByType(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<String> datesString = handleDates(startDate, endDate);
        List<ReportData> retVal = logService.getLogsTotalCountByType(datesString.get(0), datesString.get(1));
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @RequestMapping(value="/logs/total/hostname", method = RequestMethod.GET)
    public ResponseEntity<List<ReportData>> getLogsTotalCountByHostname(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<String> datesString = handleDates(startDate, endDate);
        List<ReportData> retVal = logService.getLogsTotalCountByHostname(datesString.get(0), datesString.get(1));
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    private List<String> handleDates(LocalDateTime startDate, LocalDateTime endDate){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        if (startDate == null){
            // Thu Jan 01 1970 01:00:00 UTC
            startDate = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.ofHours(0));
        }
        if (endDate == null){
            endDate = LocalDateTime.now();
        }
        String startDateString = formatter.format(startDate);
        String endDateString = formatter.format(endDate);
        return new ArrayList<>(Arrays.asList(startDateString, endDateString));
    }

}
