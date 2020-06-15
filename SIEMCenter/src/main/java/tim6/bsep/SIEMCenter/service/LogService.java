package tim6.bsep.SIEMCenter.service;

import tim6.bsep.SIEMCenter.model.Log;

public interface LogService {

    void save(Log log);

    Log findById(Long id);
}
