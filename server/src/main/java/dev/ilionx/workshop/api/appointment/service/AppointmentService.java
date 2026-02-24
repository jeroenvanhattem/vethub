package dev.ilionx.workshop.api.appointment.service;

import dev.ilionx.workshop.api.appointment.model.Appointment;
import dev.ilionx.workshop.api.appointment.model.AppointmentStatus;
import dev.ilionx.workshop.api.appointment.model.request.CreateAppointmentRequest;
import dev.ilionx.workshop.api.appointment.model.request.UpdateAppointmentRequest;
import dev.ilionx.workshop.api.appointment.repository.AppointmentRepository;
import dev.ilionx.workshop.api.pet.model.Pet;
import dev.ilionx.workshop.api.pet.repository.PetRepository;
import dev.ilionx.workshop.api.vet.model.Vet;
import dev.ilionx.workshop.api.vet.repository.VetRepository;
import io.github.jframe.exception.core.DataNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing appointments.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentService {

    private static final String APPOINTMENT_NOT_FOUND_MSG = "Appointment not found with id: ";
    private static final String PET_NOT_FOUND_MSG = "Pet not found with id: ";
    private static final String VET_NOT_FOUND_MSG = "Vet not found with id: ";

    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;
    private final VetRepository vetRepository;

    /**
     * Find all appointments.
     *
     * @return list of all appointments
     */
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    /**
     * Find appointment by ID.
     *
     * @param id the appointment ID
     * @return the appointment
     * @throws DataNotFoundException if appointment not found
     */
    public Appointment findById(final Integer id) {
        return appointmentRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException(APPOINTMENT_NOT_FOUND_MSG + id));
    }

    /**
     * Find all appointments for a specific pet.
     *
     * @param petId the pet ID
     * @return list of appointments
     */
    public List<Appointment> findByPetId(final Integer petId) {
        return appointmentRepository.findByPetId(petId);
    }

    /**
     * Find all appointments for a specific vet.
     *
     * @param vetId the vet ID
     * @return list of appointments
     */
    public List<Appointment> findByVetId(final Integer vetId) {
        return appointmentRepository.findByVetId(vetId);
    }

    /**
     * Create a new appointment.
     *
     * @param request the create request
     * @return the created appointment
     * @throws DataNotFoundException if pet or vet not found
     */
    public Appointment create(final CreateAppointmentRequest request) {
        final Pet pet = petRepository.findById(request.getPetId())
            .orElseThrow(() -> new DataNotFoundException(PET_NOT_FOUND_MSG + request.getPetId()));

        final Vet vet = vetRepository.findById(request.getVetId())
            .orElseThrow(() -> new DataNotFoundException(VET_NOT_FOUND_MSG + request.getVetId()));

        final Appointment appointment = new Appointment();
        appointment.setScheduledDateTime(request.getScheduledDateTime());
        appointment.setReason(request.getReason());
        appointment.setStatus(
            request.getStatus() != null ? request.getStatus() : AppointmentStatus.SCHEDULED
        );
        appointment.setPet(pet);
        appointment.setVet(vet);

        return appointmentRepository.save(appointment);
    }

    /**
     * Update an existing appointment.
     *
     * @param id      the appointment ID
     * @param request the update request
     * @return the updated appointment
     * @throws DataNotFoundException if appointment not found
     */
    public Appointment update(final Integer id, final UpdateAppointmentRequest request) {
        final Appointment appointment = findById(id);

        appointment.setScheduledDateTime(request.getScheduledDateTime());
        appointment.setReason(request.getReason());
        appointment.setStatus(request.getStatus());

        return appointmentRepository.save(appointment);
    }

    /**
     * Cancel an appointment.
     *
     * @param id the appointment ID
     * @return the cancelled appointment
     * @throws DataNotFoundException if appointment not found
     */
    public Appointment cancel(final Integer id) {
        final Appointment appointment = findById(id);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);
    }

    /**
     * Mark an appointment as completed.
     *
     * @param id the appointment ID
     * @return the completed appointment
     * @throws DataNotFoundException if appointment not found
     */
    public Appointment complete(final Integer id) {
        final Appointment appointment = findById(id);
        appointment.setStatus(AppointmentStatus.COMPLETED);
        return appointmentRepository.save(appointment);
    }

    /**
     * Delete an appointment.
     *
     * @param id the appointment ID
     * @throws DataNotFoundException if appointment not found
     */
    public void delete(final Integer id) {
        final Appointment appointment = findById(id);
        appointmentRepository.delete(appointment);
    }

}
