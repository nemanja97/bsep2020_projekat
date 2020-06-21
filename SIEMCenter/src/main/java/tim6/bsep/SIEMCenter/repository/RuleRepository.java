package tim6.bsep.SIEMCenter.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tim6.bsep.SIEMCenter.model.Rule;

@Repository
public interface RuleRepository extends MongoRepository<Rule, Long> {

}
