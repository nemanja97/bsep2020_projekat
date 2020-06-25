package tim6.bsep.SIEMCenter.model;

import java.util.ArrayList;

public class LogList {

    private ArrayList<Log> logs;

    public LogList() {
    }

    public LogList(ArrayList<Log> logs) {
        this.logs = logs;
    }

    public ArrayList<Log> getLogs() {
        return logs;
    }

    public void setLogs(ArrayList<Log> logs) {
        this.logs = logs;
    }
}
