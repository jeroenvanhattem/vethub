package dev.ilionx.workshop.api.pet.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Request DTO for creating a new pet type.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Request to create a new pet type")
public class CreatePetTypeRequest {

    @Schema(
        description = "Name of the pet type",
        example = "Rabbit",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

}
