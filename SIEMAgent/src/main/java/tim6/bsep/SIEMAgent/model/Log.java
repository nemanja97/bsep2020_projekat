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

    public Log(Date timestamp, FacilityType facilityType, SeverityLevel severityLevel, String hostname, String message, LogType type) {
        this.timestamp = timestamp;
        this.facilityType = facilityType;
        this.severityLevel = severityLevel;
        this.hostname = hostname;
        this.message = message;
        this.type = type;
    }

    public Log(String line, boolean simulated){
        String[] tokens = line.split(" ");
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        try{
            String[] facilitySeverity = tokens[0].split("\\.");

            this.facilityType = FacilityType.valueOf(facilitySeverity[0]);
            this.severityLevel = SeverityLevel.valueOf(facilitySeverity[1]);

            try {
                this.timestamp = df.parse(tokens[1]);
            }catch (ParseException e){
                this.timestamp = new Date();
            }
            this.hostname = tokens[2];
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
        } catch (Exception e){
            this.facilityType = FacilityType.SECURITY;
            this.severityLevel = SeverityLevel.WARNING;
            this.timestamp = new Date();
            this.hostname = System.getProperty("user.name");
            this.message = "ERROR PARSING LOG: " +  line;
            if(simulated){
                this.type = LogType.SIMULATED;
            }else {
                this.type = LogType.SYSTEM;
            }
        }

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
