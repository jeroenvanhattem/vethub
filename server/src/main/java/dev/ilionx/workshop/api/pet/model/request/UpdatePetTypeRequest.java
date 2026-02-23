package dev.ilionx.workshop.api.pet.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Request DTO for updating an existing pet type.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Request to update an existing pet type")
public class UpdatePetTypeRequest {

    @Schema(
        description = "Name of the pet type",
        example = "Bunny",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

}
