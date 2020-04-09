package tim6.bsep.pki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tim6.bsep.pki.model.CertificateInfo;

import java.util.List;

public interface CertificateInfoRepository extends JpaRepository<CertificateInfo, Long> {

}
