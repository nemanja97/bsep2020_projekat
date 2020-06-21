package tim6.bsep.SIEMAgent.model;

import com.sun.jna.platform.win32.Advapi32Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Log {
    private Date timestamp;
    private FacilityType facilityType;
    private SeverityLevel severityLevel;
    private String hostname;
    private String message;
    private LogType type;

    public Log(){

    }

    public Log(String line, boolean simulated){
        String[] tokens = line.split(" ");
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        try {
            this.timestamp = df.parse(tokens[0]);
        }catch (ParseException e){
            this.timestamp = new Date();
        }
        this.facilityType = FacilityType.valueOf(tokens[1]);
        this.severityLevel = SeverityLevel.valueOf(tokens[2]);
        this.hostname = tokens[3];
        if(tokens.length > 5){
            StringBuilder sb = new StringBuilder(tokens[4]);
            for (int i = 5; i < tokens.length; i++) {
                sb.append(" ").append(tokens[i]);
            }
            this.message = sb.toString();
        }else {
            this.message = tokens[4];
        }
        if(simulated){
            this.type = LogType.SIMULATED;
        }else {
            this.type = LogType.SYSTEM;
        }
    }

    public Log(Advapi32Util.EventLogRecord record){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        this.timestamp = new Date(record.getRecord().TimeGenerated.longValue() * 1000);
        this.facilityType = parseSource(record.getSource());
        this.severityLevel = parseType(record.getType().toString());
        this.hostname = System.getProperty("user.name");
        StringBuilder message = new StringBuilder();
        if(record.getStrings() != null){
            for (String s: record.getStrings()) {
                message.append(" ").append(s.replaceAll("\\s+", " "));
            }
        }
        this.message = message.toString();
        this.type = LogType.SYSTEM;
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

    private FacilityType parseSource(String source){
        if(source.toLowerCase().contains("security")){
            return FacilityType.SECURITY;
        }else{
            return FacilityType.KERN;
        }
    }

    private SeverityLevel parseType(String type){
        switch (type){
            case "AuditFailure":
            case "Error":
                return SeverityLevel.ERROR;
            case "Warning":
                return SeverityLevel.WARNING;
            default:
                return SeverityLevel.INFORMATIONAL;
        }
    }
}
