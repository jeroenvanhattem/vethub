package dev.ilionx.workshop.api.appointment.model.response;

import dev.ilionx.workshop.api.appointment.model.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Response DTO for appointment data.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "Appointment response")
public class AppointmentResponse {

    @Schema(
        description = "Appointment ID",
        example = "1"
    )
    private Integer id;

    @Schema(
        description = "Scheduled date and time of the appointment",
        example = "2026-03-01T10:00:00"
    )
    private LocalDateTime scheduledDateTime;

    @Schema(
        description = "Reason for the appointment",
        example = "Annual checkup"
    )
    private String reason;

    @Schema(
        description = "Status of the appointment",
        example = "SCHEDULED"
    )
    private AppointmentStatus status;

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

    @Schema(
        description = "Vet ID",
        example = "1"
    )
    private Integer vetId;

    @Schema(
        description = "Vet first name",
        example = "John"
    )
    private String vetFirstName;

    @Schema(
        description = "Vet last name",
        example = "Doe"
    )
    private String vetLastName;

}
