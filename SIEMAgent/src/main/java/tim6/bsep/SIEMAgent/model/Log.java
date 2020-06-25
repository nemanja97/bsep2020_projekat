package tim6.bsep.SIEMAgent.model;

import com.sun.jna.platform.win32.Advapi32Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Log {

    private long id = 0L;
    private Date timestamp;
    private FacilityType facilityType;
    private SeverityLevel severityLevel;
    private String hostname;
    private String message;
    private LogType type;

    public Log(){

    }

    public Log(Date timestamp, FacilityType facilityType, SeverityLevel severityLevel, String hostname, String message, LogType type) {
        this.timestamp = timestamp;
        this.facilityType = facilityType;
        this.severityLevel = severityLevel;
        this.hostname = hostname;
        this.message = message;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Log{" +
                "timestamp=" + timestamp +
                ", facilityType='" + facilityType + '\'' +
                ", severityLevel='" + severityLevel + '\'' +
                ", hostname='" + hostname + '\'' +
                ", message='" + message + '\'' +
                ", type=" + type +
                '}';
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public FacilityType getFacilityType() {
        return facilityType;
    }

    public void getFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
    }

    public SeverityLevel getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(SeverityLevel severityLevel) {
        this.severityLevel = severityLevel;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }
}
