package dev.ilionx.workshop.api.owner.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Request DTO for creating a new pet owner.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Request to create a new owner")
public class CreateOwnerRequest {

    @Schema(
        description = "Owner's first name",
        example = "George",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String firstName;

    @Schema(
        description = "Owner's last name",
        example = "Franklin",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String lastName;

    @Schema(
        description = "Owner's street address",
        example = "110 W. Liberty St.",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String address;

    @Schema(
        description = "Owner's city",
        example = "Madison",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String city;

    @Schema(
        description = "Owner's telephone number",
        example = "6085551023",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String telephone;

    @Schema(
        description = "Owner's email address",
        example = "george.franklin@example.com",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String email;

}
