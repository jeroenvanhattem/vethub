package dev.ilionx.workshop.api.vet.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Request DTO for creating a new veterinarian.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Request to create a new vet")
public class CreateVetRequest {

    @Schema(
        description = "Vet's first name",
        example = "Alice",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String firstName;

    @Schema(
        description = "Vet's last name",
        example = "Brown",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String lastName;

    @Schema(
        description = "List of specialty IDs to assign to the vet",
        example = "[1, 2]",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private List<Integer> specialtyIds;

}
