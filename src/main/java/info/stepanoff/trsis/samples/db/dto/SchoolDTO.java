package info.stepanoff.trsis.samples.db.dto;

import info.stepanoff.trsis.samples.db.model.School;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 *
 * @author wildhost5
 */
public class SchoolDTO {

    @Getter
    private final Integer id;
    @Getter
    private final Integer number;
    @Getter
    private final String name;
    @Getter
    private final Iterable<Integer> batches;

    public SchoolDTO(School school) {
        this.id = school.getId();
        this.number = school.getNumber();
        this.name = school.getName();

        if (school.getBatchesList() == null) {
            batches = Collections.emptyList();
        } else {
            batches = school.getBatchesList()
                    .stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());
        }

    }
}
