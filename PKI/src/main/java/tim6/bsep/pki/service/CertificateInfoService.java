package tim6.bsep.pki.service;

import tim6.bsep.pki.exceptions.CertificateNotFoundException;
import tim6.bsep.pki.model.CertificateInfo;
import tim6.bsep.pki.model.CertificateInfoWithChildren;
import tim6.bsep.pki.model.RevocationReason;

import java.util.Map;

public interface CertificateInfoService {

    Map<CertificateInfo, CertificateInfoWithChildren> findAll(Boolean onlyValid);

    CertificateInfo findById(Long id);

    CertificateInfo findByAlias(String alias);

    CertificateInfo findByAliasIgnoreCase(String alias);

    CertificateInfo save(CertificateInfo certInfo);

    CertificateInfo revoke(Long id, RevocationReason revocationReason) throws CertificateNotFoundException;
}
