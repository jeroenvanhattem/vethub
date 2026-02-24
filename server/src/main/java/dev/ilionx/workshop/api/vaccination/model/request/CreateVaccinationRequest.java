package dev.ilionx.workshop.api.vaccination.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

/**
 * Request DTO for creating a new vaccination.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Request to create a new vaccination")
public class CreateVaccinationRequest {

    @NotBlank(message = "Vaccine name cannot be blank")
    @Schema(
        description = "Name of the vaccine",
        example = "Rabies",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String vaccineName;

    @NotNull(message = "Vaccination date cannot be null")
    @Past(message = "Vaccination date must be in the past")
    @Schema(
        description = "Date of vaccination",
        example = "2023-01-01",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate vaccinationDate;

    @Schema(
        description = "Next due date for this vaccine",
        example = "2024-01-01",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private LocalDate nextDueDate;

    @Schema(
        description = "ID of the pet",
        example = "1",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Integer petId;

}
