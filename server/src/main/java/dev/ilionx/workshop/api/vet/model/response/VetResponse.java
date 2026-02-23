package dev.ilionx.workshop.api.vet.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Response DTO containing veterinarian details.
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Response containing veterinarian details")
public class VetResponse {

    @Schema(
        description = "The unique identifier of the vet",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer id;

    @Schema(
        description = "The vet's first name",
        example = "James",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String firstName;

    @Schema(
        description = "The vet's last name",
        example = "Carter",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String lastName;

    @Schema(description = "The vet's specialties")
    private List<SpecialtyResponse> specialties;
}
