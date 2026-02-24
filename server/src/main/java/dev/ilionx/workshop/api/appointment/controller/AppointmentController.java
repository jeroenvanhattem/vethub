package dev.ilionx.workshop.api.appointment.controller;

import dev.ilionx.workshop.api.appointment.model.Appointment;
import dev.ilionx.workshop.api.appointment.model.mapper.AppointmentMapper;
import dev.ilionx.workshop.api.appointment.model.request.CreateAppointmentRequest;
import dev.ilionx.workshop.api.appointment.model.request.UpdateAppointmentRequest;
import dev.ilionx.workshop.api.appointment.model.response.AppointmentResponse;
import dev.ilionx.workshop.api.appointment.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dev.ilionx.workshop.api.Paths.APPOINTMENTS;
import static dev.ilionx.workshop.api.Paths.APPOINTMENT_BY_ID;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for managing appointments.
 */
@Tag(
    name = "Appointment",
    description = "Appointment scheduling and management endpoints"
)
@RestController
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @ResponseStatus(OK)
    @Operation(
        summary = "Get all appointments",
        description = "Returns all appointments in the system"
    )
    @GetMapping(
        path = APPOINTMENTS,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        final List<Appointment> appointments = appointmentService.findAll();
        return ResponseEntity.status(OK).body(appointmentMapper.toResponseList(appointments));
    }

    @ResponseStatus(OK)
    @Operation(
        summary = "Get appointment by ID",
        description = "Returns a single appointment by its ID"
    )
    @GetMapping(
        path = APPOINTMENT_BY_ID,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AppointmentResponse> getAppointmentById(
        @PathVariable final Integer appointmentId
    ) {
        final Appointment appointment = appointmentService.findById(appointmentId);
        return ResponseEntity.status(OK).body(appointmentMapper.toResponse(appointment));
    }

    @ResponseStatus(CREATED)
    @Operation(
        summary = "Create appointment",
        description = "Creates a new appointment for a pet with a vet"
    )
    @PostMapping(
        path = APPOINTMENTS,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AppointmentResponse> createAppointment(
        @Valid
        @RequestBody final CreateAppointmentRequest request
    ) {
        final Appointment createdAppointment = appointmentService.create(request);
        return ResponseEntity.status(CREATED).body(appointmentMapper.toResponse(createdAppointment));
    }

    @ResponseStatus(OK)
    @Operation(
        summary = "Update appointment",
        description = "Updates an existing appointment"
    )
    @PutMapping(
        path = APPOINTMENT_BY_ID,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AppointmentResponse> updateAppointment(
        @PathVariable final Integer appointmentId,
        @Valid
        @RequestBody final UpdateAppointmentRequest request
    ) {
        final Appointment appointment = appointmentService.update(appointmentId, request);
        return ResponseEntity.status(OK).body(appointmentMapper.toResponse(appointment));
    }

    @ResponseStatus(OK)
    @Operation(
        summary = "Cancel appointment",
        description = "Cancels an appointment by setting its status to CANCELLED"
    )
    @PatchMapping(
        path = APPOINTMENT_BY_ID + "/cancel",
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AppointmentResponse> cancelAppointment(
        @PathVariable final Integer appointmentId
    ) {
        final Appointment appointment = appointmentService.cancel(appointmentId);
        return ResponseEntity.status(OK).body(appointmentMapper.toResponse(appointment));
    }

    @ResponseStatus(OK)
    @Operation(
        summary = "Complete appointment",
        description = "Marks an appointment as completed by setting its status to COMPLETED"
    )
    @PatchMapping(
        path = APPOINTMENT_BY_ID + "/complete",
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AppointmentResponse> completeAppointment(
        @PathVariable final Integer appointmentId
    ) {
        final Appointment appointment = appointmentService.complete(appointmentId);
        return ResponseEntity.status(OK).body(appointmentMapper.toResponse(appointment));
    }

    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Delete appointment",
        description = "Deletes an appointment from the system"
    )
    @DeleteMapping(path = APPOINTMENT_BY_ID)
    public ResponseEntity<Void> deleteAppointment(
        @PathVariable final Integer appointmentId
    ) {
        appointmentService.delete(appointmentId);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
