package tim6.bsep.pki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tim6.bsep.pki.model.ExpirableLink;

public interface ExpirableLinkRepository extends JpaRepository<ExpirableLink, Long> {

    ExpirableLink findByLink(String link);
}
