package dev.ilionx.workshop.api.appointment.model.mapper;

import dev.ilionx.workshop.api.appointment.model.Appointment;
import dev.ilionx.workshop.api.appointment.model.response.AppointmentResponse;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for Appointment entity to response DTO.
 */
@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(
        target = "petId",
        source = "pet.id"
    )
    @Mapping(
        target = "petName",
        source = "pet.name"
    )
    @Mapping(
        target = "vetId",
        source = "vet.id"
    )
    @Mapping(
        target = "vetFirstName",
        source = "vet.firstName"
    )
    @Mapping(
        target = "vetLastName",
        source = "vet.lastName"
    )
    AppointmentResponse toResponse(Appointment appointment);

    List<AppointmentResponse> toResponseList(List<Appointment> appointments);

}
