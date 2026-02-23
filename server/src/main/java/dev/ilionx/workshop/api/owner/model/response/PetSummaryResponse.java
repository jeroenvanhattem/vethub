package dev.ilionx.workshop.api.owner.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * Response DTO containing summary of a pet within an owner response.
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Summary of a pet within an owner response")
public class PetSummaryResponse {

    @Schema(
        description = "The unique identifier of the pet",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer id;

    @Schema(
        description = "The pet's name",
        example = "Leo",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @Schema(
        description = "The pet's birth date",
        example = "2020-09-07",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate birthDate;

    @Schema(
        description = "The pet type name",
        example = "Cat",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String typeName;
}
