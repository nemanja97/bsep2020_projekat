package tim6.bsep.SIEMCenter.model.drools;

import lombok.Getter;
import lombok.Setter;
import tim6.bsep.SIEMCenter.model.Log;

@Getter
@Setter
public class LogWrapper {

    private Log log;

    private boolean handled;

    public LogWrapper(){ }

    public LogWrapper(Log log){
        this.log = log;
        this.handled = false;
    }

    public LogWrapper(Log log, boolean handled){
        this.log = log;
        this.handled = handled;
    }
}
