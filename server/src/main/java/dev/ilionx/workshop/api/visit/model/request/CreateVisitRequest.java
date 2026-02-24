package dev.ilionx.workshop.api.visit.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for creating a new visit.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Request to create a new visit")
public class CreateVisitRequest {

    @NotNull(message = "Date cannot be null")
    @Schema(
        description = "Date of the visit",
        example = "2023-01-01",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate date;

    @NotBlank(message = "Description cannot be blank")
    @Schema(
        description = "Description of the visit",
        example = "Rabies shot",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String description;

    @Schema(
        description = "ID of the pet",
        example = "1",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Integer petId;

}
