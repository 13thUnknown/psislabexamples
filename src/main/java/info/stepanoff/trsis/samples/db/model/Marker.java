package info.stepanoff.trsis.samples.db.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class Marker extends BaseLocalizationEntity {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

}
