package tim6.bsep.SIEMCenter.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tim6.bsep.SIEMCenter.model.Blacklist;

@Repository
public interface BlacklistRepository extends MongoRepository<Blacklist, Long> {

    Blacklist findByName(String name);

}
