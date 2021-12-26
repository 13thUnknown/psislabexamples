package info.stepanoff.trsis.samples.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@Document("Localization")
@CompoundIndex(name = "localization_index", def = "{'locale' : 1, 'mnemocode': 1, '_class': 1}", unique = true)
public abstract class BaseLocalizationEntity {

    @Id
    @JsonIgnore
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String locale;

    @NotNull
    private String mnemocode;

    public String sequenceName() {
        return "localization_sequence";
    }

}
