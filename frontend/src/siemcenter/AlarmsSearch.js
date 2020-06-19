import React from "react";
import Moment from "moment";
import momentLocalizer from "react-widgets-moment";
import { Multiselect, DateTimePicker } from "react-widgets";

Moment.locale("en");
momentLocalizer();

const AlarmsSearch = (props) => {
  const facilityType = [
    'KERN',
    'USER',
    'MAIL',
    'DAEMON',
    'AUTH',
    'SYSLOG',
    'LPR',
    'NEWS',
    'UUCP',
    'CRON',
    'AUTHPRIV',
    'FTP',
    'NTP',
    'SECURITY',
    'CONSOLE',
    'SOLARIS_CRON',
    'LOCAL0',
    'LOCAL1',
    'LOCAL2',
    'LOCAL3',
    'LOCAL4',
    'LOCAL5',
    'LOCAL6',
    'LOCAL7',
  ];

  const severityLevel = [
    'EMERGENCY',
    'ALERT',
    'CRITICAL',
    'ERROR',
    'WARNING',
    'NOTICE',
    'INFORMATIONAL',
    'DEBUG',
  ];

  const logType = [
    'SYSTEM',
    'SIMULATED',
  ];

  return (
    <>
      <div class="field is-grouped">
        <div class="control">
          <div class="control">
            <Multiselect data={facilityType} placeholder="Facility" />
          </div>
        </div>
        <div class="control">
          <Multiselect data={severityLevel} placeholder="Severity" />
        </div>
        <div class="control">
          <input class="input" type="text" placeholder="Message regex" />
        </div>
        <div class="control">
          <input class="input" type="text" placeholder="Hostnames" />
        </div>
        <div class="control">
          <Multiselect data={logType} placeholder="Log type" />
        </div>
        <div class="control">
          <DateTimePicker placeholder="From" />
        </div>
        <div class="control">
          <DateTimePicker placeholder="To" />
        </div>
        <div class="control">
          <button class="button is-primary">Submit</button>
        </div>
      </div>
    </>
  );
};

export default AlarmsSearch;
