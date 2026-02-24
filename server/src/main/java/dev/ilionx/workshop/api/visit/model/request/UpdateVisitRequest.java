package dev.ilionx.workshop.api.visit.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for updating an existing visit.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Request to update an existing visit")
public class UpdateVisitRequest {

    @NotNull(message = "Date cannot be null")
    @Schema(
        description = "Date of the visit",
        example = "2023-06-15",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate date;

    @NotBlank(message = "Description cannot be blank")
    @Schema(
        description = "Description of the visit",
        example = "Follow-up checkup",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String description;

}
