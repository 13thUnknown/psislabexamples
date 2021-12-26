package info.stepanoff.trsis.samples.service.db;

import info.stepanoff.trsis.samples.db.model.DatabaseSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SequenceGeneratorServiceImpl implements SequenceGeneratorService {
    private final MongoOperations mongoOperations;

    @Override
    public long generateSequence(String seqName) {
        DatabaseSequence counter = this.mongoOperations.findAndModify(Query.query(Criteria.where("_id").is(seqName)),
                (new Update()).inc("seq", 1), FindAndModifyOptions.options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return Optional.ofNullable(counter).map(DatabaseSequence::getSeq).orElse(1L);
    }
}
