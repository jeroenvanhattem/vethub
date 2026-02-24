package dev.ilionx.workshop.api.vaccination.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * Response DTO for vaccination data.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Vaccination response")
public class VaccinationResponse {

    @Schema(
        description = "Vaccination ID",
        example = "1"
    )
    private Integer id;

    @Schema(
        description = "Name of the vaccine",
        example = "Rabies"
    )
    private String vaccineName;

    @Schema(
        description = "Date of vaccination",
        example = "2023-01-01"
    )
    private LocalDate vaccinationDate;

    @Schema(
        description = "Next due date for this vaccine",
        example = "2024-01-01"
    )
    private LocalDate nextDueDate;

    @Schema(
        description = "Pet ID",
        example = "1"
    )
    private Integer petId;

    @Schema(
        description = "Pet name",
        example = "Leo"
    )
    private String petName;

}
