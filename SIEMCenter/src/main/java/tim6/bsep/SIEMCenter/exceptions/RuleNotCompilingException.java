package tim6.bsep.SIEMCenter.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.kie.api.builder.Message;

import java.util.List;

@Getter
@Setter
public class RuleNotCompilingException extends Exception {

    private static final long serialVersionUID = 400951665004493381L;
    List<Message> reason;

    public RuleNotCompilingException(List<Message> reasonBuildFailed) {
        super("Rule build failed");
        reason = reasonBuildFailed;
    }
}
