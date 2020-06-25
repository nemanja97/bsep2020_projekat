package tim6.bsep.pki.service;

import tim6.bsep.pki.exceptions.ExpirableLinkNotPresentException;
import tim6.bsep.pki.model.ExpirableLink;

import java.util.List;

public interface ExpirableLinkService {

    void createExpirableLink();

    void updateExpirableLinkWithCertificateId(String link, Long certificateId) throws ExpirableLinkNotPresentException;

    boolean isExpirableLinkUsed(String link);

    ExpirableLink getExpirableLinkByLink(String link);

    List<ExpirableLink> getExpirableLinks();
}
