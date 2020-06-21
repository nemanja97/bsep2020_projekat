package tim6.bsep.SIEMCenter.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tim6.bsep.SIEMCenter.model.Blacklist;

public interface BlacklistRepository extends MongoRepository<Blacklist, Long> {

    Blacklist findByName(String name);

}
