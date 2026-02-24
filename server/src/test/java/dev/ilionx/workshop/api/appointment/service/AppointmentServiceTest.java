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
import dev.ilionx.workshop.support.UnitTest;
import io.github.jframe.exception.core.DataNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("Unit Test - Appointment Service")
class AppointmentServiceTest extends UnitTest {

    private static final Integer VALID_APPOINTMENT_ID = 1;
    private static final Integer VALID_PET_ID = 1;
    private static final Integer VALID_VET_ID = 1;
    private static final Integer NON_EXISTENT_APPOINTMENT_ID = 999;
    private static final Integer NON_EXISTENT_PET_ID = 999;
    private static final Integer NON_EXISTENT_VET_ID = 999;
    private static final LocalDateTime VALID_SCHEDULED_DATE_TIME = LocalDateTime.of(2024, 6, 15, 14, 30);
    private static final String VALID_REASON = "Annual checkup";

    private AppointmentRepository appointmentRepository;
    private PetRepository petRepository;
    private VetRepository vetRepository;
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        appointmentRepository = mock(AppointmentRepository.class);
        petRepository = mock(PetRepository.class);
        vetRepository = mock(VetRepository.class);
        appointmentService = new AppointmentService(appointmentRepository, petRepository, vetRepository);
    }

    @Test
    @DisplayName("Should return all appointments when appointments exist")
    void shouldReturnAllAppointmentsWhenAppointmentsExist() {
        // Given: Two appointments exist in the repository
        final Appointment firstAppointment = aValidAppointment();
        final Appointment secondAppointment = aValidAppointment();
        secondAppointment.setId(2);
        secondAppointment.setReason("Vaccination");
        final List<Appointment> expectedAppointments = List.of(firstAppointment, secondAppointment);
        given(appointmentRepository.findAll()).willReturn(expectedAppointments);

        // When: Finding all appointments
        final List<Appointment> actualAppointments = appointmentService.findAll();

        // Then: Both appointments should be returned
        assertThat(actualAppointments, is(notNullValue()));
        assertThat(actualAppointments, hasSize(2));
        assertThat(actualAppointments.get(0).getId(), is(equalTo(VALID_APPOINTMENT_ID)));
        assertThat(actualAppointments.get(0).getReason(), is(equalTo(VALID_REASON)));
        assertThat(actualAppointments.get(1).getId(), is(equalTo(2)));
        assertThat(actualAppointments.get(1).getReason(), is(equalTo("Vaccination")));
    }

    @Test
    @DisplayName("Should return empty list when no appointments exist")
    void shouldReturnEmptyListWhenNoAppointmentsExist() {
        // Given: No appointments exist
        given(appointmentRepository.findAll()).willReturn(Collections.emptyList());

        // When: Finding all appointments
        final List<Appointment> actualAppointments = appointmentService.findAll();

        // Then: An empty list should be returned
        assertThat(actualAppointments, is(notNullValue()));
        assertThat(actualAppointments, is(empty()));
    }

    @Test
    @DisplayName("Should return appointment when appointment exists")
    void shouldReturnAppointmentWhenAppointmentExists() {
        // Given: An appointment exists in the repository
        final Appointment expectedAppointment = aValidAppointment();
        given(appointmentRepository.findById(VALID_APPOINTMENT_ID)).willReturn(Optional.of(expectedAppointment));

        // When: Finding appointment by ID
        final Appointment actualAppointment = appointmentService.findById(VALID_APPOINTMENT_ID);

        // Then: The appointment should be returned
        assertThat(actualAppointment, is(notNullValue()));
        assertThat(actualAppointment.getId(), is(equalTo(VALID_APPOINTMENT_ID)));
        assertThat(actualAppointment.getReason(), is(equalTo(VALID_REASON)));
        assertThat(actualAppointment.getScheduledDateTime(), is(equalTo(VALID_SCHEDULED_DATE_TIME)));
        assertThat(actualAppointment.getStatus(), is(equalTo(AppointmentStatus.SCHEDULED)));
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when appointment does not exist")
    void shouldThrowDataNotFoundExceptionWhenAppointmentDoesNotExist() {
        // Given: No appointment exists with the given ID
        given(appointmentRepository.findById(NON_EXISTENT_APPOINTMENT_ID)).willReturn(Optional.empty());

        // When/Then: Should throw DataNotFoundException
        assertThrows(
            DataNotFoundException.class,
            () -> appointmentService.findById(NON_EXISTENT_APPOINTMENT_ID)
        );
    }

    @Test
    @DisplayName("Should return appointments when pet has appointments")
    void shouldReturnAppointmentsWhenPetHasAppointments() {
        // Given: A pet has 2 appointments
        final Appointment firstAppointment = aValidAppointment();
        final Appointment secondAppointment = aValidAppointment();
        secondAppointment.setId(2);
        final List<Appointment> expectedAppointments = List.of(firstAppointment, secondAppointment);
        given(appointmentRepository.findByPetId(VALID_PET_ID)).willReturn(expectedAppointments);

        // When: Finding appointments by pet ID
        final List<Appointment> actualAppointments = appointmentService.findByPetId(VALID_PET_ID);

        // Then: Both appointments should be returned
        assertThat(actualAppointments, is(notNullValue()));
        assertThat(actualAppointments, hasSize(2));
    }

    @Test
    @DisplayName("Should return empty list when pet has no appointments")
    void shouldReturnEmptyListWhenPetHasNoAppointments() {
        // Given: A pet has no appointments
        given(appointmentRepository.findByPetId(VALID_PET_ID)).willReturn(Collections.emptyList());

        // When: Finding appointments by pet ID
        final List<Appointment> actualAppointments = appointmentService.findByPetId(VALID_PET_ID);

        // Then: An empty list should be returned
        assertThat(actualAppointments, is(notNullValue()));
        assertThat(actualAppointments, is(empty()));
    }

    @Test
    @DisplayName("Should return appointments when vet has appointments")
    void shouldReturnAppointmentsWhenVetHasAppointments() {
        // Given: A vet has 2 appointments
        final Appointment firstAppointment = aValidAppointment();
        final Appointment secondAppointment = aValidAppointment();
        secondAppointment.setId(2);
        final List<Appointment> expectedAppointments = List.of(firstAppointment, secondAppointment);
        given(appointmentRepository.findByVetId(VALID_VET_ID)).willReturn(expectedAppointments);

        // When: Finding appointments by vet ID
        final List<Appointment> actualAppointments = appointmentService.findByVetId(VALID_VET_ID);

        // Then: Both appointments should be returned
        assertThat(actualAppointments, is(notNullValue()));
        assertThat(actualAppointments, hasSize(2));
    }

    @Test
    @DisplayName("Should return empty list when vet has no appointments")
    void shouldReturnEmptyListWhenVetHasNoAppointments() {
        // Given: A vet has no appointments
        given(appointmentRepository.findByVetId(VALID_VET_ID)).willReturn(Collections.emptyList());

        // When: Finding appointments by vet ID
        final List<Appointment> actualAppointments = appointmentService.findByVetId(VALID_VET_ID);

        // Then: An empty list should be returned
        assertThat(actualAppointments, is(notNullValue()));
        assertThat(actualAppointments, is(empty()));
    }

    @Test
    @DisplayName("Should create appointment when request is valid")
    void shouldCreateAppointmentWhenRequestIsValid() {
        // Given: A valid create appointment request
        final CreateAppointmentRequest request = new CreateAppointmentRequest()
            .setPetId(VALID_PET_ID)
            .setVetId(VALID_VET_ID)
            .setScheduledDateTime(VALID_SCHEDULED_DATE_TIME)
            .setReason(VALID_REASON)
            .setStatus(AppointmentStatus.SCHEDULED);
        final Pet pet = aValidPet();
        final Vet vet = aValidVet();
        final Appointment savedAppointment = aValidAppointment();
        given(petRepository.findById(VALID_PET_ID)).willReturn(Optional.of(pet));
        given(vetRepository.findById(VALID_VET_ID)).willReturn(Optional.of(vet));
        given(appointmentRepository.save(any(Appointment.class))).willReturn(savedAppointment);

        // When: Creating an appointment
        final Appointment createdAppointment = appointmentService.create(request);

        // Then: Appointment should be created and returned
        assertThat(createdAppointment, is(notNullValue()));
        assertThat(createdAppointment.getReason(), is(equalTo(VALID_REASON)));
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should create appointment with default status when status not provided")
    void shouldCreateAppointmentWithDefaultStatusWhenStatusNotProvided() {
        // Given: A create request without status
        final CreateAppointmentRequest request = new CreateAppointmentRequest()
            .setPetId(VALID_PET_ID)
            .setVetId(VALID_VET_ID)
            .setScheduledDateTime(VALID_SCHEDULED_DATE_TIME)
            .setReason(VALID_REASON);
        final Pet pet = aValidPet();
        final Vet vet = aValidVet();
        final Appointment savedAppointment = aValidAppointment();
        given(petRepository.findById(VALID_PET_ID)).willReturn(Optional.of(pet));
        given(vetRepository.findById(VALID_VET_ID)).willReturn(Optional.of(vet));
        given(appointmentRepository.save(any(Appointment.class))).willReturn(savedAppointment);

        // When: Creating an appointment
        final Appointment createdAppointment = appointmentService.create(request);

        // Then: Appointment should be created with default SCHEDULED status
        assertThat(createdAppointment, is(notNullValue()));
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when creating appointment for non-existent pet")
    void shouldThrowDataNotFoundExceptionWhenCreatingAppointmentForNonExistentPet() {
        // Given: No pet exists with the given ID
        final CreateAppointmentRequest request = new CreateAppointmentRequest()
            .setPetId(NON_EXISTENT_PET_ID)
            .setVetId(VALID_VET_ID)
            .setScheduledDateTime(VALID_SCHEDULED_DATE_TIME)
            .setReason(VALID_REASON);
        given(petRepository.findById(NON_EXISTENT_PET_ID)).willReturn(Optional.empty());

        // When/Then: Should throw DataNotFoundException
        assertThrows(
            DataNotFoundException.class,
            () -> appointmentService.create(request)
        );
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when creating appointment for non-existent vet")
    void shouldThrowDataNotFoundExceptionWhenCreatingAppointmentForNonExistentVet() {
        // Given: Pet exists but vet does not
        final CreateAppointmentRequest request = new CreateAppointmentRequest()
            .setPetId(VALID_PET_ID)
            .setVetId(NON_EXISTENT_VET_ID)
            .setScheduledDateTime(VALID_SCHEDULED_DATE_TIME)
            .setReason(VALID_REASON);
        final Pet pet = aValidPet();
        given(petRepository.findById(VALID_PET_ID)).willReturn(Optional.of(pet));
        given(vetRepository.findById(NON_EXISTENT_VET_ID)).willReturn(Optional.empty());

        // When/Then: Should throw DataNotFoundException
        assertThrows(
            DataNotFoundException.class,
            () -> appointmentService.create(request)
        );
    }

    @Test
    @DisplayName("Should update appointment when request is valid")
    void shouldUpdateAppointmentWhenRequestIsValid() {
        // Given: An appointment exists and a valid update request
        final Appointment existingAppointment = aValidAppointment();
        final UpdateAppointmentRequest request = new UpdateAppointmentRequest()
            .setScheduledDateTime(LocalDateTime.of(2024, 7, 20, 10, 0))
            .setReason("Updated reason")
            .setStatus(AppointmentStatus.CONFIRMED);
        given(appointmentRepository.findById(VALID_APPOINTMENT_ID)).willReturn(Optional.of(existingAppointment));
        given(appointmentRepository.save(any(Appointment.class))).willReturn(existingAppointment);

        // When: Updating the appointment
        final Appointment updatedAppointment = appointmentService.update(VALID_APPOINTMENT_ID, request);

        // Then: Appointment should be updated
        assertThat(updatedAppointment, is(notNullValue()));
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when updating non-existent appointment")
    void shouldThrowDataNotFoundExceptionWhenUpdatingNonExistentAppointment() {
        // Given: No appointment exists with the given ID
        final UpdateAppointmentRequest request = new UpdateAppointmentRequest()
            .setScheduledDateTime(LocalDateTime.of(2024, 7, 20, 10, 0))
            .setReason("Updated reason")
            .setStatus(AppointmentStatus.CONFIRMED);
        given(appointmentRepository.findById(NON_EXISTENT_APPOINTMENT_ID)).willReturn(Optional.empty());

        // When/Then: Should throw DataNotFoundException
        assertThrows(
            DataNotFoundException.class,
            () -> appointmentService.update(NON_EXISTENT_APPOINTMENT_ID, request)
        );
    }

    @Test
    @DisplayName("Should cancel appointment when appointment exists")
    void shouldCancelAppointmentWhenAppointmentExists() {
        // Given: An appointment exists
        final Appointment appointment = aValidAppointment();
        given(appointmentRepository.findById(VALID_APPOINTMENT_ID)).willReturn(Optional.of(appointment));
        given(appointmentRepository.save(any(Appointment.class))).willReturn(appointment);

        // When: Cancelling the appointment
        final Appointment cancelledAppointment = appointmentService.cancel(VALID_APPOINTMENT_ID);

        // Then: Appointment status should be set to CANCELLED
        assertThat(cancelledAppointment, is(notNullValue()));
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when cancelling non-existent appointment")
    void shouldThrowDataNotFoundExceptionWhenCancellingNonExistentAppointment() {
        // Given: No appointment exists with the given ID
        given(appointmentRepository.findById(NON_EXISTENT_APPOINTMENT_ID)).willReturn(Optional.empty());

        // When/Then: Should throw DataNotFoundException
        assertThrows(
            DataNotFoundException.class,
            () -> appointmentService.cancel(NON_EXISTENT_APPOINTMENT_ID)
        );
    }

    @Test
    @DisplayName("Should complete appointment when appointment exists")
    void shouldCompleteAppointmentWhenAppointmentExists() {
        // Given: An appointment exists
        final Appointment appointment = aValidAppointment();
        given(appointmentRepository.findById(VALID_APPOINTMENT_ID)).willReturn(Optional.of(appointment));
        given(appointmentRepository.save(any(Appointment.class))).willReturn(appointment);

        // When: Completing the appointment
        final Appointment completedAppointment = appointmentService.complete(VALID_APPOINTMENT_ID);

        // Then: Appointment status should be set to COMPLETED
        assertThat(completedAppointment, is(notNullValue()));
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when completing non-existent appointment")
    void shouldThrowDataNotFoundExceptionWhenCompletingNonExistentAppointment() {
        // Given: No appointment exists with the given ID
        given(appointmentRepository.findById(NON_EXISTENT_APPOINTMENT_ID)).willReturn(Optional.empty());

        // When/Then: Should throw DataNotFoundException
        assertThrows(
            DataNotFoundException.class,
            () -> appointmentService.complete(NON_EXISTENT_APPOINTMENT_ID)
        );
    }

    @Test
    @DisplayName("Should delete appointment when appointment exists")
    void shouldDeleteAppointmentWhenAppointmentExists() {
        // Given: An appointment exists
        final Appointment appointment = aValidAppointment();
        given(appointmentRepository.findById(VALID_APPOINTMENT_ID)).willReturn(Optional.of(appointment));

        // When: Deleting the appointment
        appointmentService.delete(VALID_APPOINTMENT_ID);

        // Then: Appointment should be deleted
        verify(appointmentRepository).delete(appointment);
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when deleting non-existent appointment")
    void shouldThrowDataNotFoundExceptionWhenDeletingNonExistentAppointment() {
        // Given: No appointment exists with the given ID
        given(appointmentRepository.findById(NON_EXISTENT_APPOINTMENT_ID)).willReturn(Optional.empty());

        // When/Then: Should throw DataNotFoundException
        assertThrows(
            DataNotFoundException.class,
            () -> appointmentService.delete(NON_EXISTENT_APPOINTMENT_ID)
        );
    }

    private Appointment aValidAppointment() {
        final Appointment appointment = new Appointment();
        appointment.setId(VALID_APPOINTMENT_ID);
        appointment.setScheduledDateTime(VALID_SCHEDULED_DATE_TIME);
        appointment.setReason(VALID_REASON);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setPet(aValidPet());
        appointment.setVet(aValidVet());
        return appointment;
    }
}
