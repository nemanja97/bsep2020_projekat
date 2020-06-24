package tim6.bsep.SIEMAgent.utility;

import com.sun.jna.platform.win32.Advapi32Util;
import tim6.bsep.SIEMAgent.configuration.AgentConfigItem;
import tim6.bsep.SIEMAgent.model.FacilityType;
import tim6.bsep.SIEMAgent.model.Log;
import tim6.bsep.SIEMAgent.model.LogType;
import tim6.bsep.SIEMAgent.model.SeverityLevel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingUtility {

    public static Log parse(String line, AgentConfigItem item) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        Pattern r = Pattern.compile(item.getPattern());
        Matcher m = r.matcher(line);
        LogType type;
        if(item.getSimulated()){
            type = LogType.SIMULATED;
        }else {
            type = LogType.SYSTEM;
        }
        if (m.find( )) {
            FacilityType facilityType = null;
            SeverityLevel severityLevel = null;
            Date timestamp = null;
            String hostname = null;
            String message = null;

            for (int i = 1; i <= m.groupCount(); i++) {
                switch (item.getGroups().get(i - 1)) {
                    case "F":
                        facilityType = FacilityType.valueOf(m.group(i));
                        break;
                    case "S":
                        severityLevel = SeverityLevel.valueOf(m.group(i));
                        break;
                    case "T":
                        try {
                            timestamp = df.parse(m.group(i));
                        }catch (ParseException e){
                            timestamp = new Date();
                        }
                        break;
                    case "H":
                        hostname = m.group(i);
                        break;
                    case "M":
                        message = m.group(i);
                        break;
                    default:
                        break;
                }
            }
            if(!item.getGroups().contains("T")){
                timestamp = new Date();
            }
            if(!item.getGroups().contains("H")){
                hostname = System.getProperty("user.name");
            }

            return new Log(timestamp, facilityType, severityLevel, hostname, message, type);
        } else {
            return new Log(new Date(),
                    FacilityType.SECURITY,
                    SeverityLevel.WARNING,
                    System.getProperty("user.name"), "ERROR PARSING: " + line,
                    type);
        }
    }

    public static Log parse(Advapi32Util.EventLogRecord record){
        Date timestamp = new Date(record.getRecord().TimeGenerated.longValue() * 1000);
        FacilityType facilityType = parseSource(record.getSource());
        SeverityLevel severityLevel = parseType(record.getType().toString());
        String hostname = System.getProperty("user.name");
        StringBuilder message = new StringBuilder();
        if(record.getStrings() != null){
            for (String s: record.getStrings()) {
                message.append(" ").append(s.replaceAll("\\s+", " "));
            }
        }
        LogType type = LogType.SYSTEM;
        return new Log(timestamp, facilityType, severityLevel, hostname, message.toString(), type);
    }

    public static FacilityType parseSource(String source){
        if(source.toLowerCase().contains("security")){
            return FacilityType.SECURITY;
        }else{
            return FacilityType.KERN;
        }
    }

    public static SeverityLevel parseType(String type){
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
