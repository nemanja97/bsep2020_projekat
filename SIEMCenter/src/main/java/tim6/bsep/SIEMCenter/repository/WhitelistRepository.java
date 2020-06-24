package tim6.bsep.SIEMCenter.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tim6.bsep.SIEMCenter.model.Whitelist;

@Repository
public interface WhitelistRepository extends MongoRepository<Whitelist, Long> {

    Whitelist findByName(String name);

}

