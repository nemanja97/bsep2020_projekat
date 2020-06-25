package tim6.bsep.pki.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tim6.bsep.pki.exceptions.ExpirableLinkNotPresentException;
import tim6.bsep.pki.model.ExpirableLink;
import tim6.bsep.pki.repository.ExpirableLinkRepository;
import tim6.bsep.pki.service.ExpirableLinkService;

import java.util.List;
import java.util.UUID;

@Service
public class ExpirableLinkServiceImpl implements ExpirableLinkService {

    @Autowired
    ExpirableLinkRepository expirableLinkRepository;

    @Override
    public void createExpirableLink() {
        ExpirableLink expirableLink = new ExpirableLink();
        expirableLink.setCertificateId(null);
        expirableLink.setLink(UUID.randomUUID().toString());
        expirableLinkRepository.save(expirableLink);
    }

    @Override
    public void updateExpirableLinkWithCertificateId(String link, Long certificateId) throws ExpirableLinkNotPresentException {
        ExpirableLink expirableLink = getExpirableLinkByLink(link);
        if (expirableLink == null)
            throw new ExpirableLinkNotPresentException();
        expirableLink.setCertificateId(certificateId);
        expirableLinkRepository.save(expirableLink);
    }

    @Override
    public boolean isExpirableLinkUsed(String link) {
        ExpirableLink expirableLink = expirableLinkRepository.findByLink(link);
        return (expirableLink != null && expirableLink.getCertificateId() == null);
    }

    @Override
    public ExpirableLink getExpirableLinkByLink(String link) {
        return expirableLinkRepository.findByLink(link);
    }

    @Override
    public List<ExpirableLink> getExpirableLinks() {
        return expirableLinkRepository.findAll();
    }
}
