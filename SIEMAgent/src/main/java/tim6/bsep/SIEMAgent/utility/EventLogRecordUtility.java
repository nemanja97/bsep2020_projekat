package tim6.bsep.SIEMAgent.utility;

import com.sun.jna.platform.win32.Advapi32Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class EventLogRecordUtility {
    public static String getDescription(Advapi32Util.EventLogRecord record){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);

        StringBuilder retVal = new StringBuilder();
        retVal.append("Time: ").append(df.format(new Date(record.getRecord().TimeGenerated.longValue() * 1000)))
                .append(" ID: ").append((short) record.getEventId())
                .append(" Type: ").append(record.getType())
                .append(" Source: ").append(record.getSource())
                .append(" Code: ").append((short) record.getStatusCode())
                .append(" Message:");

        if(record.getStrings() != null){
            for (String s: record.getStrings()) {
                retVal.append(" ").append(s.replaceAll("\\s+", " "));
            }
        }

        return retVal.toString();
    }
}
