package tim6.bsep.SIEMCenter.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import tim6.bsep.SIEMCenter.model.sequences.AlarmSequences;
import tim6.bsep.SIEMCenter.model.sequences.BlacklistSequences;
import tim6.bsep.SIEMCenter.model.sequences.LogSequences;
import tim6.bsep.SIEMCenter.model.sequences.WhitelistSequences;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class NextSequenceService {

    @Autowired
    private MongoOperations mongo;

    public long logGetNextSequence()
    {
        LogSequences counter = mongo.findAndModify(
                query(where("_id").is("logSequences")),
                new Update().inc("seq",1),
                options().returnNew(true).upsert(true),
                LogSequences.class);
        return counter.getSeq();
    }

    public long alarmGetNextSequence()
    {
        AlarmSequences counter = mongo.findAndModify(
                query(where("_id").is("alarmSequences")),
                new Update().inc("seq",1),
                options().returnNew(true).upsert(true),
                AlarmSequences.class);
        return counter.getSeq();
    }

    public long whiteListGetNextSequence()
    {
        WhitelistSequences counter = mongo.findAndModify(
                query(where("_id").is("whitelistSequences")),
                new Update().inc("seq",1),
                options().returnNew(true).upsert(true),
                WhitelistSequences.class);
        return counter.getSeq();
    }

    public long blackListGetNextSequence()
    {
        BlacklistSequences counter = mongo.findAndModify(
                query(where("_id").is("blacklistSequences")),
                new Update().inc("seq",1),
                options().returnNew(true).upsert(true),
                BlacklistSequences.class);
        return counter.getSeq();
    }
}
