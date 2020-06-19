package tim6.bsep.SIEMCenter.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import tim6.bsep.SIEMCenter.model.Log;

@Repository
public interface LogsRepository extends MongoRepository<Log, Long>, QuerydslPredicateExecutor<Log> {

}
