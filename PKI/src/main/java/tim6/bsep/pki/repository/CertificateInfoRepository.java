package tim6.bsep.pki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tim6.bsep.pki.model.CertificateInfo;

public interface CertificateInfoRepository extends JpaRepository<CertificateInfo, Long> {
}
