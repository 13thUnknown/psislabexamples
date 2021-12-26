package info.stepanoff.trsis.samples.db.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("DatabaseSequence")
public class DatabaseSequence {
    @Id
    private String id;
    private long seq = 1L;
}