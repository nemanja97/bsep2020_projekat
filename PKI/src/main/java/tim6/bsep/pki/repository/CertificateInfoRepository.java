package tim6.bsep.pki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tim6.bsep.pki.model.CertificateInfo;

import java.util.Date;
import java.util.List;

public interface CertificateInfoRepository extends JpaRepository<CertificateInfo, Long> {

    List<CertificateInfo> findAllByEndDateBeforeAndRevoked(Date date, boolean isRevoked);

    CertificateInfo findByAlias(String alias);

    CertificateInfo findFirstByAliasContainingIgnoreCase(String alias);

}
