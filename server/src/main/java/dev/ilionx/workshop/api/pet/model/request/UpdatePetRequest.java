package dev.ilionx.workshop.api.pet.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * Request DTO for updating an existing pet.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Request to update an existing pet")
public class UpdatePetRequest {

    @Schema(
        description = "Pet's name",
        example = "Max",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @Schema(
        description = "Pet's date of birth",
        example = "2020-09-07",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate birthDate;

    @Schema(
        description = "ID of the pet type",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer typeId;

}
