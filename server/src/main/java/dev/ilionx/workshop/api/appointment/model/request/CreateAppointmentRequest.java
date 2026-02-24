package dev.ilionx.workshop.api.appointment.model.request;

import dev.ilionx.workshop.api.appointment.model.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for creating a new appointment.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Request to create a new appointment")
public class CreateAppointmentRequest {

    @NotNull(message = "Scheduled date and time cannot be null")
    @Future(message = "Scheduled date and time must be in the future")
    @Schema(
        description = "Scheduled date and time of the appointment",
        example = "2026-03-01T10:00:00",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDateTime scheduledDateTime;

    @NotBlank(message = "Reason cannot be blank")
    @Schema(
        description = "Reason for the appointment",
        example = "Annual checkup",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String reason;

    @Schema(
        description = "Status of the appointment",
        example = "SCHEDULED",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        defaultValue = "SCHEDULED"
    )
    private AppointmentStatus status;

    @NotNull(message = "Pet ID cannot be null")
    @Schema(
        description = "ID of the pet",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer petId;

    @NotNull(message = "Vet ID cannot be null")
    @Schema(
        description = "ID of the vet",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer vetId;

}
