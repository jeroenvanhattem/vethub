package dev.ilionx.workshop.api.pet.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Response DTO containing pet type details.
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Response containing pet type details")
public class PetTypeResponse {

    @Schema(
        description = "The unique identifier of the pet type",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer id;

    @Schema(
        description = "The pet type name",
        example = "Cat",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;
}
