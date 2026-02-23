package dev.ilionx.workshop.api.vet.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Response DTO containing specialty details.
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Response containing specialty details")
public class SpecialtyResponse {

    @Schema(
        description = "The unique identifier of the specialty",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer id;

    @Schema(
        description = "The specialty name",
        example = "Radiology",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;
}
