package dev.ilionx.workshop.api.vet.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Request DTO for updating an existing specialty.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Request to update an existing specialty")
public class UpdateSpecialtyRequest {

    @Schema(
        description = "Name of the specialty",
        example = "Cardiology",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

}
