package dev.ilionx.workshop.api.vaccination.service;

import dev.ilionx.workshop.api.pet.model.Pet;
import dev.ilionx.workshop.api.pet.repository.PetRepository;
import dev.ilionx.workshop.api.vaccination.model.Vaccination;
import dev.ilionx.workshop.api.vaccination.model.request.CreateVaccinationRequest;
import dev.ilionx.workshop.api.vaccination.model.request.UpdateVaccinationRequest;
import dev.ilionx.workshop.api.vaccination.repository.VaccinationRepository;
import dev.ilionx.workshop.support.UnitTest;
import io.github.jframe.exception.core.DataNotFoundException;

import java.time.LocalDate;
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

@DisplayName("Unit Test - Vaccination Service")
class VaccinationServiceTest extends UnitTest {

    private static final Integer VALID_PET_ID = 1;
    private static final Integer VALID_VACCINATION_ID = 1;
    private static final LocalDate VALID_VACCINATION_DATE = LocalDate.of(2023, 1, 1);
    private static final String VALID_VACCINE_NAME = "Rabies";
    private static final Integer NON_EXISTENT_PET_ID = 999;
    private static final Integer NON_EXISTENT_VACCINATION_ID = 999;

    private PetRepository petRepository;
    private VaccinationRepository vaccinationRepository;
    private VaccinationService vaccinationService;

    @BeforeEach
    void setUp() {
        petRepository = mock(PetRepository.class);
        vaccinationRepository = mock(VaccinationRepository.class);
        vaccinationService = new VaccinationService(petRepository, vaccinationRepository);
    }

    @Test
    @DisplayName("Should return vaccinations when pet has vaccinations")
    void shouldReturnVaccinationsWhenPetHasVaccinations() {
        // Given: A pet exists with 2 vaccinations in the repository
        final Pet pet = aValidPet();
        final Vaccination firstVaccination = aValidVaccination();
        final Vaccination secondVaccination = aValidVaccination();
        secondVaccination.setId(2);
        secondVaccination.setVaccineName("DHPP");
        final List<Vaccination> expectedVaccinations = List.of(firstVaccination, secondVaccination);
        given(petRepository.findById(VALID_PET_ID)).willReturn(Optional.of(pet));
        given(vaccinationRepository.findByPetId(VALID_PET_ID)).willReturn(expectedVaccinations);

        // When: Finding vaccinations by pet ID
        final List<Vaccination> actualVaccinations = vaccinationService.findByPetId(VALID_PET_ID);

        // Then: Both vaccinations should be returned
        assertThat(actualVaccinations, is(notNullValue()));
        assertThat(actualVaccinations, hasSize(2));
        assertThat(actualVaccinations.get(0).getId(), is(equalTo(VALID_VACCINATION_ID)));
        assertThat(actualVaccinations.get(0).getVaccineName(), is(equalTo(VALID_VACCINE_NAME)));
        assertThat(actualVaccinations.get(1).getId(), is(equalTo(2)));
        assertThat(actualVaccinations.get(1).getVaccineName(), is(equalTo("DHPP")));
    }

    @Test
    @DisplayName("Should return empty list when pet has no vaccinations")
    void shouldReturnEmptyListWhenPetHasNoVaccinations() {
        // Given: A pet exists with no vaccinations
        final Pet pet = aValidPet();
        given(petRepository.findById(VALID_PET_ID)).willReturn(Optional.of(pet));
        given(vaccinationRepository.findByPetId(VALID_PET_ID)).willReturn(Collections.emptyList());

        // When: Finding vaccinations by pet ID
        final List<Vaccination> actualVaccinations = vaccinationService.findByPetId(VALID_PET_ID);

        // Then: An empty list should be returned
        assertThat(actualVaccinations, is(notNullValue()));
        assertThat(actualVaccinations, is(empty()));
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when pet does not exist for findByPetId")
    void shouldThrowDataNotFoundExceptionWhenPetDoesNotExistForFindByPetId() {
        // Given: No pet exists with the given ID
        given(petRepository.findById(NON_EXISTENT_PET_ID)).willReturn(Optional.empty());

        // When/Then: Should throw DataNotFoundException
        assertThrows(
            DataNotFoundException.class,
            () -> vaccinationService.findByPetId(NON_EXISTENT_PET_ID)
        );
    }

    @Test
    @DisplayName("Should return vaccination when vaccination exists")
    void shouldReturnVaccinationWhenVaccinationExists() {
        // Given: A vaccination exists in the repository
        final Vaccination expectedVaccination = aValidVaccination();
        given(vaccinationRepository.findById(VALID_VACCINATION_ID)).willReturn(Optional.of(expectedVaccination));

        // When: Finding vaccination by ID
        final Vaccination actualVaccination = vaccinationService.findById(VALID_VACCINATION_ID);

        // Then: The vaccination should be returned
        assertThat(actualVaccination, is(notNullValue()));
        assertThat(actualVaccination.getId(), is(equalTo(VALID_VACCINATION_ID)));
        assertThat(actualVaccination.getVaccineName(), is(equalTo(VALID_VACCINE_NAME)));
        assertThat(actualVaccination.getVaccinationDate(), is(equalTo(VALID_VACCINATION_DATE)));
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when vaccination does not exist")
    void shouldThrowDataNotFoundExceptionWhenVaccinationDoesNotExist() {
        // Given: No vaccination exists with the given ID
        given(vaccinationRepository.findById(NON_EXISTENT_VACCINATION_ID)).willReturn(Optional.empty());

        // When/Then: Should throw DataNotFoundException
        assertThrows(
            DataNotFoundException.class,
            () -> vaccinationService.findById(NON_EXISTENT_VACCINATION_ID)
        );
    }

    @Test
    @DisplayName("Should create vaccination when request is valid")
    void shouldCreateVaccinationWhenRequestIsValid() {
        // Given: A valid create vaccination request
        final CreateVaccinationRequest request = new CreateVaccinationRequest()
            .setVaccineName(VALID_VACCINE_NAME)
            .setVaccinationDate(VALID_VACCINATION_DATE)
            .setNextDueDate(LocalDate.of(2024, 1, 1));
        final Pet pet = aValidPet();
        final Vaccination savedVaccination = aValidVaccination();
        given(petRepository.findById(VALID_PET_ID)).willReturn(Optional.of(pet));
        given(vaccinationRepository.save(any(Vaccination.class))).willReturn(savedVaccination);

        // When: Creating a vaccination
        final Vaccination createdVaccination = vaccinationService.create(VALID_PET_ID, request);

        // Then: Vaccination should be created and returned
        assertThat(createdVaccination, is(notNullValue()));
        assertThat(createdVaccination.getVaccineName(), is(equalTo(VALID_VACCINE_NAME)));
        verify(vaccinationRepository).save(any(Vaccination.class));
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when creating vaccination for non-existent pet")
    void shouldThrowDataNotFoundExceptionWhenCreatingVaccinationForNonExistentPet() {
        // Given: No pet exists with the given ID
        final CreateVaccinationRequest request = new CreateVaccinationRequest()
            .setVaccineName(VALID_VACCINE_NAME)
            .setVaccinationDate(VALID_VACCINATION_DATE);
        given(petRepository.findById(NON_EXISTENT_PET_ID)).willReturn(Optional.empty());

        // When/Then: Should throw DataNotFoundException
        assertThrows(
            DataNotFoundException.class,
            () -> vaccinationService.create(NON_EXISTENT_PET_ID, request)
        );
    }

    @Test
    @DisplayName("Should update vaccination when request is valid")
    void shouldUpdateVaccinationWhenRequestIsValid() {
        // Given: A vaccination exists and a valid update request
        final Vaccination existingVaccination = aValidVaccination();
        final UpdateVaccinationRequest request = new UpdateVaccinationRequest()
            .setVaccineName("Updated Vaccine")
            .setVaccinationDate(LocalDate.of(2023, 6, 15))
            .setNextDueDate(LocalDate.of(2024, 6, 15));
        given(vaccinationRepository.findById(VALID_VACCINATION_ID)).willReturn(Optional.of(existingVaccination));
        given(vaccinationRepository.save(any(Vaccination.class))).willReturn(existingVaccination);

        // When: Updating the vaccination
        final Vaccination updatedVaccination = vaccinationService.update(VALID_VACCINATION_ID, request);

        // Then: Vaccination should be updated
        assertThat(updatedVaccination, is(notNullValue()));
        verify(vaccinationRepository).save(any(Vaccination.class));
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when updating non-existent vaccination")
    void shouldThrowDataNotFoundExceptionWhenUpdatingNonExistentVaccination() {
        // Given: No vaccination exists with the given ID
        final UpdateVaccinationRequest request = new UpdateVaccinationRequest()
            .setVaccineName("Updated Vaccine")
            .setVaccinationDate(LocalDate.of(2023, 6, 15));
        given(vaccinationRepository.findById(NON_EXISTENT_VACCINATION_ID)).willReturn(Optional.empty());

        // When/Then: Should throw DataNotFoundException
        assertThrows(
            DataNotFoundException.class,
            () -> vaccinationService.update(NON_EXISTENT_VACCINATION_ID, request)
        );
    }

    @Test
    @DisplayName("Should delete vaccination when vaccination exists")
    void shouldDeleteVaccinationWhenVaccinationExists() {
        // Given: A vaccination exists
        final Vaccination vaccination = aValidVaccination();
        given(vaccinationRepository.findById(VALID_VACCINATION_ID)).willReturn(Optional.of(vaccination));

        // When: Deleting the vaccination
        vaccinationService.delete(VALID_VACCINATION_ID);

        // Then: Vaccination should be deleted
        verify(vaccinationRepository).delete(vaccination);
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when deleting non-existent vaccination")
    void shouldThrowDataNotFoundExceptionWhenDeletingNonExistentVaccination() {
        // Given: No vaccination exists with the given ID
        given(vaccinationRepository.findById(NON_EXISTENT_VACCINATION_ID)).willReturn(Optional.empty());

        // When/Then: Should throw DataNotFoundException
        assertThrows(
            DataNotFoundException.class,
            () -> vaccinationService.delete(NON_EXISTENT_VACCINATION_ID)
        );
    }

    private Vaccination aValidVaccination() {
        final Vaccination vaccination = new Vaccination();
        vaccination.setId(VALID_VACCINATION_ID);
        vaccination.setVaccineName(VALID_VACCINE_NAME);
        vaccination.setVaccinationDate(VALID_VACCINATION_DATE);
        vaccination.setNextDueDate(LocalDate.of(2024, 1, 1));
        vaccination.setPet(aValidPet());
        return vaccination;
    }
}
