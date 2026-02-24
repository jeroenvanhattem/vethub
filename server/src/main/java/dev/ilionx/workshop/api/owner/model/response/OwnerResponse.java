package dev.ilionx.workshop.api.owner.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Response DTO containing owner details with their pets.
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Response containing owner details with their pets")
public class OwnerResponse {

    @Schema(
        description = "The unique identifier of the owner",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer id;

    @Schema(
        description = "The owner's first name",
        example = "George",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String firstName;

    @Schema(
        description = "The owner's last name",
        example = "Franklin",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String lastName;

    @Schema(
        description = "The owner's address",
        example = "110 W. Liberty St.",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String address;

    @Schema(
        description = "The owner's city",
        example = "Madison",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String city;

    @Schema(
        description = "The owner's telephone number",
        example = "6085551023",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String telephone;

    @Schema(
        description = "The owner's email address",
        example = "george.franklin@example.com",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Schema(description = "The owner's pets")
    private List<PetSummaryResponse> pets;
}
