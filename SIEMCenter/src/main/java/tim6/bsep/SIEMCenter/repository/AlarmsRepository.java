package tim6.bsep.SIEMCenter.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import tim6.bsep.SIEMCenter.model.drools.Alarm;

import java.util.List;

@Repository
public interface AlarmsRepository extends MongoRepository<Alarm, Long>, QuerydslPredicateExecutor<Alarm> {

    Alarm findFirstByOrderByTimestampDesc();

}
