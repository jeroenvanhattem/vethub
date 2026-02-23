package dev.ilionx.workshop.api.vet.service;

import dev.ilionx.workshop.api.vet.model.Specialty;
import dev.ilionx.workshop.api.vet.model.request.CreateSpecialtyRequest;
import dev.ilionx.workshop.api.vet.model.request.UpdateSpecialtyRequest;
import dev.ilionx.workshop.api.vet.repository.SpecialtyRepository;
import dev.ilionx.workshop.support.UnitTest;
import io.github.jframe.exception.core.DataNotFoundException;

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

@DisplayName("Unit Test - Specialty Service")
class SpecialtyServiceTest extends UnitTest {

    private static final Integer VALID_SPECIALTY_ID = 1;
    private static final String VALID_SPECIALTY_NAME = "Radiology";
    private static final Integer NON_EXISTENT_SPECIALTY_ID = 999;

    private SpecialtyRepository specialtyRepository;
    private SpecialtyService specialtyService;

    @BeforeEach
    void setUp() {
        specialtyRepository = mock(SpecialtyRepository.class);
        specialtyService = new SpecialtyService(specialtyRepository);
    }

    @Test
    @DisplayName("Should return all specialties when specialties exist")
    void shouldReturnAllSpecialtiesWhenSpecialtiesExist() {
        // Given: Multiple specialties exist in the repository
        final Specialty firstSpecialty = aValidSpecialty();
        final Specialty secondSpecialty = aValidSpecialty();
        secondSpecialty.setId(2);
        secondSpecialty.setName("Surgery");
        final List<Specialty> expectedSpecialties = List.of(firstSpecialty, secondSpecialty);
        given(specialtyRepository.findAll()).willReturn(expectedSpecialties);

        // When: Finding all specialties
        final List<Specialty> actualSpecialties = specialtyService.findAll();

        // Then: All specialties should be returned
        assertThat(actualSpecialties, is(notNullValue()));
        assertThat(actualSpecialties, hasSize(2));
        assertThat(actualSpecialties.get(0).getId(), is(equalTo(VALID_SPECIALTY_ID)));
        assertThat(actualSpecialties.get(0).getName(), is(equalTo(VALID_SPECIALTY_NAME)));
        assertThat(actualSpecialties.get(1).getId(), is(equalTo(2)));
        assertThat(actualSpecialties.get(1).getName(), is(equalTo("Surgery")));
    }

    @Test
    @DisplayName("Should return empty list when no specialties exist")
    void shouldReturnEmptyListWhenNoSpecialtiesExist() {
        // Given: No specialties exist in the repository
        given(specialtyRepository.findAll()).willReturn(Collections.emptyList());

        // When: Finding all specialties
        final List<Specialty> actualSpecialties = specialtyService.findAll();

        // Then: An empty list should be returned
        assertThat(actualSpecialties, is(notNullValue()));
        assertThat(actualSpecialties, is(empty()));
    }

    @Test
    @DisplayName("Should return specialty when valid ID exists")
    void shouldReturnSpecialtyWhenValidIdExists() {
        // Given: A specialty with valid ID exists in the repository
        final Specialty expectedSpecialty = aValidSpecialty();
        given(specialtyRepository.findById(VALID_SPECIALTY_ID)).willReturn(Optional.of(expectedSpecialty));

        // When: Finding specialty by ID
        final Specialty actualSpecialty = specialtyService.findById(VALID_SPECIALTY_ID);

        // Then: The specialty should be found with all fields
        assertThat(actualSpecialty, is(notNullValue()));
        assertThat(actualSpecialty.getId(), is(equalTo(VALID_SPECIALTY_ID)));
        assertThat(actualSpecialty.getName(), is(equalTo(VALID_SPECIALTY_NAME)));
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when specialty ID does not exist")
    void shouldThrowDataNotFoundExceptionWhenSpecialtyIdDoesNotExist() {
        // Given: No specialty exists with the given ID
        given(specialtyRepository.findById(NON_EXISTENT_SPECIALTY_ID)).willReturn(Optional.empty());

        // When & Then: Finding by non-existent ID should throw DataNotFoundException
        final DataNotFoundException exception = assertThrows(
            DataNotFoundException.class,
            () -> specialtyService.findById(NON_EXISTENT_SPECIALTY_ID)
        );

        assertThat(exception.getMessage(), is(equalTo("The requested specialty does not exist.")));
    }

    @Test
    @DisplayName("Should create specialty when valid data provided")
    void shouldCreateSpecialtyWhenValidDataProvided() {
        // Given: A valid create specialty request
        final CreateSpecialtyRequest request = new CreateSpecialtyRequest();
        request.setName("Dermatology");
        final Specialty savedSpecialty = aValidSpecialty();
        savedSpecialty.setName("Dermatology");
        given(specialtyRepository.save(any(Specialty.class))).willReturn(savedSpecialty);

        // When: Creating the specialty
        final Specialty actualSpecialty = specialtyService.create(request);

        // Then: The specialty should be saved and returned
        verify(specialtyRepository).save(any(Specialty.class));
        assertThat(actualSpecialty, is(notNullValue()));
        assertThat(actualSpecialty.getName(), is(equalTo("Dermatology")));
    }

    @Test
    @DisplayName("Should update specialty when valid data provided")
    void shouldUpdateSpecialtyWhenValidDataProvided() {
        // Given: An existing specialty and update request
        final Specialty existingSpecialty = aValidSpecialty();
        final UpdateSpecialtyRequest request = new UpdateSpecialtyRequest();
        request.setName("Cardiology");
        final Specialty updatedSpecialty = aValidSpecialty();
        updatedSpecialty.setName("Cardiology");
        given(specialtyRepository.findById(VALID_SPECIALTY_ID)).willReturn(Optional.of(existingSpecialty));
        given(specialtyRepository.save(any(Specialty.class))).willReturn(updatedSpecialty);

        // When: Updating the specialty
        final Specialty actualSpecialty = specialtyService.update(VALID_SPECIALTY_ID, request);

        // Then: The specialty should be updated and saved
        verify(specialtyRepository).save(any(Specialty.class));
        assertThat(actualSpecialty, is(notNullValue()));
        assertThat(actualSpecialty.getId(), is(equalTo(VALID_SPECIALTY_ID)));
        assertThat(actualSpecialty.getName(), is(equalTo("Cardiology")));
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when updating non-existent specialty")
    void shouldThrowDataNotFoundExceptionWhenUpdatingNonExistentSpecialty() {
        // Given: No specialty exists with the given ID
        final UpdateSpecialtyRequest request = new UpdateSpecialtyRequest();
        request.setName("Cardiology");
        given(specialtyRepository.findById(NON_EXISTENT_SPECIALTY_ID)).willReturn(Optional.empty());

        // When & Then: Updating non-existent specialty should throw DataNotFoundException
        final DataNotFoundException exception = assertThrows(
            DataNotFoundException.class,
            () -> specialtyService.update(NON_EXISTENT_SPECIALTY_ID, request)
        );

        assertThat(exception.getMessage(), is(equalTo("The requested specialty does not exist.")));
    }

    @Test
    @DisplayName("Should delete specialty when valid ID exists")
    void shouldDeleteSpecialtyWhenValidIdExists() {
        // Given: A specialty with valid ID exists in the repository
        final Specialty existingSpecialty = aValidSpecialty();
        given(specialtyRepository.findById(VALID_SPECIALTY_ID)).willReturn(Optional.of(existingSpecialty));

        // When: Deleting the specialty
        specialtyService.delete(VALID_SPECIALTY_ID);

        // Then: The specialty should be deleted from the repository
        verify(specialtyRepository).deleteById(VALID_SPECIALTY_ID);
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when deleting non-existent specialty")
    void shouldThrowDataNotFoundExceptionWhenDeletingNonExistentSpecialty() {
        // Given: No specialty exists with the given ID
        given(specialtyRepository.findById(NON_EXISTENT_SPECIALTY_ID)).willReturn(Optional.empty());

        // When & Then: Deleting non-existent specialty should throw DataNotFoundException
        final DataNotFoundException exception = assertThrows(
            DataNotFoundException.class,
            () -> specialtyService.delete(NON_EXISTENT_SPECIALTY_ID)
        );

        assertThat(exception.getMessage(), is(equalTo("The requested specialty does not exist.")));
    }

}
