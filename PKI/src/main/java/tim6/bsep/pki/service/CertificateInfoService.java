package tim6.bsep.pki.service;

import tim6.bsep.pki.model.CertificateInfo;
import tim6.bsep.pki.model.SubjectData;

public interface CertificateInfoService {

    CertificateInfo findById(Long id);

    CertificateInfo save(CertificateInfo certInfo);

    void remove(Long id);
}
