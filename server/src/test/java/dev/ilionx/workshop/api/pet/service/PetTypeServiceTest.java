package dev.ilionx.workshop.api.pet.service;

import dev.ilionx.workshop.api.pet.model.PetType;
import dev.ilionx.workshop.api.pet.model.request.CreatePetTypeRequest;
import dev.ilionx.workshop.api.pet.model.request.UpdatePetTypeRequest;
import dev.ilionx.workshop.api.pet.repository.PetTypeRepository;
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

@DisplayName("Unit Test - Pet Type Service")
class PetTypeServiceTest extends UnitTest {

    private static final Integer VALID_PET_TYPE_ID = 1;
    private static final String VALID_PET_TYPE_NAME = "Cat";
    private static final Integer NON_EXISTENT_PET_TYPE_ID = 999;

    private PetTypeRepository petTypeRepository;
    private PetTypeService petTypeService;

    @BeforeEach
    void setUp() {
        petTypeRepository = mock(PetTypeRepository.class);
        petTypeService = new PetTypeService(petTypeRepository);
    }

    @Test
    @DisplayName("Should return all pet types when pet types exist")
    void shouldReturnAllPetTypesWhenPetTypesExist() {
        // Given: Multiple pet types exist in the repository
        final PetType firstPetType = aValidPetType();
        final PetType secondPetType = aValidPetType();
        secondPetType.setId(2);
        secondPetType.setName("Dog");
        final List<PetType> expectedPetTypes = List.of(firstPetType, secondPetType);
        given(petTypeRepository.findAll()).willReturn(expectedPetTypes);

        // When: Finding all pet types
        final List<PetType> actualPetTypes = petTypeService.findAll();

        // Then: All pet types should be returned
        assertThat(actualPetTypes, is(notNullValue()));
        assertThat(actualPetTypes, hasSize(2));
        assertThat(actualPetTypes.get(0).getId(), is(equalTo(VALID_PET_TYPE_ID)));
        assertThat(actualPetTypes.get(0).getName(), is(equalTo(VALID_PET_TYPE_NAME)));
        assertThat(actualPetTypes.get(1).getId(), is(equalTo(2)));
        assertThat(actualPetTypes.get(1).getName(), is(equalTo("Dog")));
    }

    @Test
    @DisplayName("Should return empty list when no pet types exist")
    void shouldReturnEmptyListWhenNoPetTypesExist() {
        // Given: No pet types exist in the repository
        given(petTypeRepository.findAll()).willReturn(Collections.emptyList());

        // When: Finding all pet types
        final List<PetType> actualPetTypes = petTypeService.findAll();

        // Then: An empty list should be returned
        assertThat(actualPetTypes, is(notNullValue()));
        assertThat(actualPetTypes, is(empty()));
    }

    @Test
    @DisplayName("Should return pet type when valid ID exists")
    void shouldReturnPetTypeWhenValidIdExists() {
        // Given: A pet type with valid ID exists in the repository
        final PetType expectedPetType = aValidPetType();
        given(petTypeRepository.findById(VALID_PET_TYPE_ID)).willReturn(Optional.of(expectedPetType));

        // When: Finding pet type by ID
        final PetType actualPetType = petTypeService.findById(VALID_PET_TYPE_ID);

        // Then: The pet type should be found with all fields
        assertThat(actualPetType, is(notNullValue()));
        assertThat(actualPetType.getId(), is(equalTo(VALID_PET_TYPE_ID)));
        assertThat(actualPetType.getName(), is(equalTo(VALID_PET_TYPE_NAME)));
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when pet type ID does not exist")
    void shouldThrowDataNotFoundExceptionWhenPetTypeIdDoesNotExist() {
        // Given: No pet type exists with the given ID
        given(petTypeRepository.findById(NON_EXISTENT_PET_TYPE_ID)).willReturn(Optional.empty());

        // When & Then: Finding by non-existent ID should throw DataNotFoundException
        final DataNotFoundException exception = assertThrows(
            DataNotFoundException.class,
            () -> petTypeService.findById(NON_EXISTENT_PET_TYPE_ID)
        );

        assertThat(exception.getMessage(), is(equalTo("The requested pet type does not exist.")));
    }

    @Test
    @DisplayName("Should create pet type when valid data provided")
    void shouldCreatePetTypeWhenValidDataProvided() {
        // Given: A valid create pet type request
        final CreatePetTypeRequest request = new CreatePetTypeRequest();
        request.setName("Rabbit");
        final PetType savedPetType = aValidPetType();
        savedPetType.setName("Rabbit");
        given(petTypeRepository.save(any(PetType.class))).willReturn(savedPetType);

        // When: Creating the pet type
        final PetType actualPetType = petTypeService.create(request);

        // Then: The pet type should be saved and returned
        verify(petTypeRepository).save(any(PetType.class));
        assertThat(actualPetType, is(notNullValue()));
        assertThat(actualPetType.getName(), is(equalTo("Rabbit")));
    }

    @Test
    @DisplayName("Should update pet type when valid data provided")
    void shouldUpdatePetTypeWhenValidDataProvided() {
        // Given: An existing pet type and update request
        final PetType existingPetType = aValidPetType();
        final UpdatePetTypeRequest request = new UpdatePetTypeRequest();
        request.setName("Bunny");
        final PetType updatedPetType = aValidPetType();
        updatedPetType.setName("Bunny");
        given(petTypeRepository.findById(VALID_PET_TYPE_ID)).willReturn(Optional.of(existingPetType));
        given(petTypeRepository.save(any(PetType.class))).willReturn(updatedPetType);

        // When: Updating the pet type
        final PetType actualPetType = petTypeService.update(VALID_PET_TYPE_ID, request);

        // Then: The pet type should be updated and saved
        verify(petTypeRepository).save(any(PetType.class));
        assertThat(actualPetType, is(notNullValue()));
        assertThat(actualPetType.getId(), is(equalTo(VALID_PET_TYPE_ID)));
        assertThat(actualPetType.getName(), is(equalTo("Bunny")));
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when updating non-existent pet type")
    void shouldThrowDataNotFoundExceptionWhenUpdatingNonExistentPetType() {
        // Given: No pet type exists with the given ID
        final UpdatePetTypeRequest request = new UpdatePetTypeRequest();
        request.setName("Bunny");
        given(petTypeRepository.findById(NON_EXISTENT_PET_TYPE_ID)).willReturn(Optional.empty());

        // When & Then: Updating non-existent pet type should throw DataNotFoundException
        final DataNotFoundException exception = assertThrows(
            DataNotFoundException.class,
            () -> petTypeService.update(NON_EXISTENT_PET_TYPE_ID, request)
        );

        assertThat(exception.getMessage(), is(equalTo("The requested pet type does not exist.")));
    }

    @Test
    @DisplayName("Should delete pet type when valid ID exists")
    void shouldDeletePetTypeWhenValidIdExists() {
        // Given: A pet type with valid ID exists in the repository
        final PetType existingPetType = aValidPetType();
        given(petTypeRepository.findById(VALID_PET_TYPE_ID)).willReturn(Optional.of(existingPetType));

        // When: Deleting the pet type
        petTypeService.delete(VALID_PET_TYPE_ID);

        // Then: The pet type should be deleted from the repository
        verify(petTypeRepository).deleteById(VALID_PET_TYPE_ID);
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when deleting non-existent pet type")
    void shouldThrowDataNotFoundExceptionWhenDeletingNonExistentPetType() {
        // Given: No pet type exists with the given ID
        given(petTypeRepository.findById(NON_EXISTENT_PET_TYPE_ID)).willReturn(Optional.empty());

        // When & Then: Deleting non-existent pet type should throw DataNotFoundException
        final DataNotFoundException exception = assertThrows(
            DataNotFoundException.class,
            () -> petTypeService.delete(NON_EXISTENT_PET_TYPE_ID)
        );

        assertThat(exception.getMessage(), is(equalTo("The requested pet type does not exist.")));
    }

}
