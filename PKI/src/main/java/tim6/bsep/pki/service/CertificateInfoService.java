package tim6.bsep.pki.service;

import tim6.bsep.pki.exceptions.CertificateNotFoundException;
import tim6.bsep.pki.model.CertificateInfo;
import tim6.bsep.pki.model.RevocationReason;
import tim6.bsep.pki.model.SubjectData;

public interface CertificateInfoService {

    CertificateInfo findById(Long id);

    CertificateInfo save(CertificateInfo certInfo);

    CertificateInfo revoke(Long id, RevocationReason revocationReason) throws CertificateNotFoundException;
}
