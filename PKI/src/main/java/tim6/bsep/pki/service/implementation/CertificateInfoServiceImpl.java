package tim6.bsep.pki.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tim6.bsep.pki.exceptions.CertificateNotFoundException;
import tim6.bsep.pki.model.CertificateInfo;
import tim6.bsep.pki.model.CertificateInfoWithChildren;
import tim6.bsep.pki.model.RevocationReason;
import tim6.bsep.pki.repository.CertificateInfoRepository;
import tim6.bsep.pki.service.CertificateInfoService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CertificateInfoServiceImpl implements CertificateInfoService {

    @Autowired
    private CertificateInfoRepository certificateInfoRepository;

    @Override
    public Map<CertificateInfo, CertificateInfoWithChildren> findAll(Boolean onlyValid) {
        List<CertificateInfo> certificateInfos;
        if (onlyValid)
            certificateInfos = certificateInfoRepository.findAllByEndDateBeforeAndRevoked(new Date(), false);
        else
            certificateInfos = certificateInfoRepository.findAll();

        Map<CertificateInfo, List<CertificateInfo>> certificateInfoMap = new HashMap<>();
        certificateInfos.forEach(c -> certificateInfoMap.put(c, new ArrayList<>()));
        certificateInfoMap.replaceAll((k, v) -> certificateInfos.stream().filter(
                c -> c.getIssuerAlias().equals(k.getAlias()) && !c.getIssuerAlias().equals(c.getId())).collect(Collectors.toList()));

        Map<CertificateInfo, CertificateInfoWithChildren> nodeMap = new HashMap<>();

        for (Map.Entry<CertificateInfo, List<CertificateInfo>> entry : certificateInfoMap.entrySet()) {
            for (CertificateInfo child : entry.getValue()) {
                CertificateInfoWithChildren childNode = nodeMap.computeIfAbsent(child, CertificateInfoWithChildren::new);

                nodeMap.computeIfAbsent(entry.getKey(), CertificateInfoWithChildren::new)
                        .getIssued().add(childNode);
            }
        }

        return nodeMap;
    }

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
