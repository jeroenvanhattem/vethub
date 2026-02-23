package dev.ilionx.workshop.api.owner.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Request DTO for updating an existing pet owner.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Request to update an existing owner")
public class UpdateOwnerRequest {

    @Schema(
        description = "Owner's first name",
        example = "Betty",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String firstName;

    @Schema(
        description = "Owner's last name",
        example = "Davis",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String lastName;

    @Schema(
        description = "Owner's street address",
        example = "638 Cardinal Ave.",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String address;

    @Schema(
        description = "Owner's city",
        example = "Sun Prairie",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String city;

    @Schema(
        description = "Owner's telephone number",
        example = "6085551749",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String telephone;

}
