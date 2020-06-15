package tim6.bsep.SIEMCenter.mapper;

import tim6.bsep.SIEMCenter.model.Log;
import tim6.bsep.SIEMCenter.web.v1.dto.LogDTO;

public class LogMapper {

    public static Log LogFromDTO(LogDTO logDTO) {
        return new Log(logDTO.getId(), logDTO.getTimestamp(), logDTO.getFacilityType(), logDTO.getSeverityLevel(),
                logDTO.getHostname(), logDTO.getMessage(), logDTO.getType());
    }
}
