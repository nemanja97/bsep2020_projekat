package tim6.bsep.pki.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tim6.bsep.pki.model.CertificateInfo;
import tim6.bsep.pki.repository.CertificateInfoRepository;
import tim6.bsep.pki.service.CertificateInfoService;

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

    public void remove(Long id){
        certificateInfoRepository.deleteById(id);
    }
}
