package dev.ilionx.workshop.api.vet.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Request DTO for creating a new specialty.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Request to create a new specialty")
public class CreateSpecialtyRequest {

    @Schema(
        description = "Name of the specialty",
        example = "Dermatology",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

}
