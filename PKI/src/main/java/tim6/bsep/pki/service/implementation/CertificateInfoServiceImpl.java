package tim6.bsep.pki.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tim6.bsep.pki.exceptions.CertificateNotFoundException;
import tim6.bsep.pki.model.CertificateInfo;
import tim6.bsep.pki.model.RevocationReason;
import tim6.bsep.pki.repository.CertificateInfoRepository;
import tim6.bsep.pki.service.CertificateInfoService;

import java.util.List;

@Service
public class CertificateInfoServiceImpl implements CertificateInfoService {

    @Autowired
    private CertificateInfoRepository certificateInfoRepository;

    public CertificateInfo findById(Long id){
        return certificateInfoRepository.findById(id).orElse(null);
    }

    public CertificateInfo save(CertificateInfo certInfo){
        return certificateInfoRepository.save(certInfo);
    }

    public CertificateInfo revoke(Long id, RevocationReason revocationReason) throws CertificateNotFoundException {
        CertificateInfo certInfo = findById(id);
        if (certInfo == null)
            throw new CertificateNotFoundException(id.toString());
        certInfo.setRevoked(true);
        certInfo.setRevocationReason(revocationReason.toString());
        return certificateInfoRepository.save(certInfo);
    }
}
