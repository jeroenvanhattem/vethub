package dev.ilionx.workshop.api.owner.service;

import dev.ilionx.workshop.api.owner.model.Owner;
import dev.ilionx.workshop.api.owner.model.request.CreateOwnerRequest;
import dev.ilionx.workshop.api.owner.model.request.UpdateOwnerRequest;
import dev.ilionx.workshop.api.owner.repository.OwnerRepository;
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

@DisplayName("Unit Test - Owner Service")
class OwnerServiceTest extends UnitTest {

    private static final Integer VALID_OWNER_ID = 1;
    private static final String VALID_FIRST_NAME = "George";
    private static final String VALID_LAST_NAME = "Franklin";
    private static final String VALID_ADDRESS = "110 W. Liberty St.";
    private static final String VALID_CITY = "Madison";
    private static final String VALID_TELEPHONE = "6085551023";
    private static final Integer NON_EXISTENT_OWNER_ID = 999;

    private OwnerRepository ownerRepository;
    private OwnerService ownerService;

    @BeforeEach
    void setUp() {
        ownerRepository = mock(OwnerRepository.class);
        ownerService = new OwnerService(ownerRepository);
    }

    @Test
    @DisplayName("Should return all owners when owners exist")
    void shouldReturnAllOwnersWhenOwnersExist() {
        // Given: Multiple owners exist in the repository
        final Owner firstOwner = aValidOwner();
        final Owner secondOwner = aValidOwner();
        secondOwner.setId(2);
        secondOwner.setFirstName("Betty");
        secondOwner.setLastName("Davis");
        final List<Owner> expectedOwners = List.of(firstOwner, secondOwner);
        given(ownerRepository.findAll()).willReturn(expectedOwners);

        // When: Finding all owners
        final List<Owner> actualOwners = ownerService.findAll();

        // Then: All owners should be returned
        assertThat(actualOwners, is(notNullValue()));
        assertThat(actualOwners, hasSize(2));
        assertThat(actualOwners.get(0).getId(), is(equalTo(VALID_OWNER_ID)));
        assertThat(actualOwners.get(0).getFirstName(), is(equalTo(VALID_FIRST_NAME)));
        assertThat(actualOwners.get(0).getLastName(), is(equalTo(VALID_LAST_NAME)));
        assertThat(actualOwners.get(1).getId(), is(equalTo(2)));
        assertThat(actualOwners.get(1).getFirstName(), is(equalTo("Betty")));
        assertThat(actualOwners.get(1).getLastName(), is(equalTo("Davis")));
    }

    @Test
    @DisplayName("Should return empty list when no owners exist")
    void shouldReturnEmptyListWhenNoOwnersExist() {
        // Given: No owners exist in the repository
        given(ownerRepository.findAll()).willReturn(Collections.emptyList());

        // When: Finding all owners
        final List<Owner> actualOwners = ownerService.findAll();

        // Then: An empty list should be returned
        assertThat(actualOwners, is(notNullValue()));
        assertThat(actualOwners, is(empty()));
    }

    @Test
    @DisplayName("Should return owner when valid ID exists")
    void shouldReturnOwnerWhenValidIdExists() {
        // Given: An owner with valid ID exists in the repository
        final Owner expectedOwner = aValidOwner();
        given(ownerRepository.findById(VALID_OWNER_ID)).willReturn(Optional.of(expectedOwner));

        // When: Finding owner by ID
        final Owner actualOwner = ownerService.findById(VALID_OWNER_ID);

        // Then: The owner should be found with all fields
        assertThat(actualOwner, is(notNullValue()));
        assertThat(actualOwner.getId(), is(equalTo(VALID_OWNER_ID)));
        assertThat(actualOwner.getFirstName(), is(equalTo(VALID_FIRST_NAME)));
        assertThat(actualOwner.getLastName(), is(equalTo(VALID_LAST_NAME)));
        assertThat(actualOwner.getAddress(), is(equalTo(VALID_ADDRESS)));
        assertThat(actualOwner.getCity(), is(equalTo(VALID_CITY)));
        assertThat(actualOwner.getTelephone(), is(equalTo(VALID_TELEPHONE)));
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when owner ID does not exist")
    void shouldThrowDataNotFoundExceptionWhenOwnerIdDoesNotExist() {
        // Given: No owner exists with the given ID
        given(ownerRepository.findById(NON_EXISTENT_OWNER_ID)).willReturn(Optional.empty());

        // When & Then: Finding by non-existent ID should throw DataNotFoundException
        final DataNotFoundException exception = assertThrows(
            DataNotFoundException.class,
            () -> ownerService.findById(NON_EXISTENT_OWNER_ID)
        );

        assertThat(exception.getMessage(), is(equalTo("The requested owner does not exist.")));
    }

    @Test
    @DisplayName("Should create owner when valid data provided")
    void shouldCreateOwnerWhenValidDataProvided() {
        // Given: A valid create owner request
        final CreateOwnerRequest request = new CreateOwnerRequest();
        request.setFirstName(VALID_FIRST_NAME);
        request.setLastName(VALID_LAST_NAME);
        request.setAddress(VALID_ADDRESS);
        request.setCity(VALID_CITY);
        request.setTelephone(VALID_TELEPHONE);
        final Owner savedOwner = aValidOwner();
        given(ownerRepository.save(any(Owner.class))).willReturn(savedOwner);

        // When: Creating the owner
        final Owner actualOwner = ownerService.create(request);

        // Then: The owner should be saved and returned
        verify(ownerRepository).save(any(Owner.class));
        assertThat(actualOwner, is(notNullValue()));
        assertThat(actualOwner.getId(), is(equalTo(VALID_OWNER_ID)));
        assertThat(actualOwner.getFirstName(), is(equalTo(VALID_FIRST_NAME)));
        assertThat(actualOwner.getLastName(), is(equalTo(VALID_LAST_NAME)));
        assertThat(actualOwner.getAddress(), is(equalTo(VALID_ADDRESS)));
        assertThat(actualOwner.getCity(), is(equalTo(VALID_CITY)));
        assertThat(actualOwner.getTelephone(), is(equalTo(VALID_TELEPHONE)));
    }

    @Test
    @DisplayName("Should update owner when valid data provided")
    void shouldUpdateOwnerWhenValidDataProvided() {
        // Given: An existing owner and update request
        final Owner existingOwner = aValidOwner();
        final UpdateOwnerRequest request = new UpdateOwnerRequest();
        request.setFirstName("Betty");
        request.setLastName("Davis");
        request.setAddress("638 Cardinal Ave.");
        request.setCity("Sun Prairie");
        request.setTelephone("6085551749");
        final Owner updatedOwner = aValidOwner();
        updatedOwner.setFirstName("Betty");
        updatedOwner.setLastName("Davis");
        updatedOwner.setAddress("638 Cardinal Ave.");
        updatedOwner.setCity("Sun Prairie");
        updatedOwner.setTelephone("6085551749");
        given(ownerRepository.findById(VALID_OWNER_ID)).willReturn(Optional.of(existingOwner));
        given(ownerRepository.save(any(Owner.class))).willReturn(updatedOwner);

        // When: Updating the owner
        final Owner actualOwner = ownerService.update(VALID_OWNER_ID, request);

        // Then: The owner should be updated and saved
        verify(ownerRepository).save(any(Owner.class));
        assertThat(actualOwner, is(notNullValue()));
        assertThat(actualOwner.getId(), is(equalTo(VALID_OWNER_ID)));
        assertThat(actualOwner.getFirstName(), is(equalTo("Betty")));
        assertThat(actualOwner.getLastName(), is(equalTo("Davis")));
        assertThat(actualOwner.getAddress(), is(equalTo("638 Cardinal Ave.")));
        assertThat(actualOwner.getCity(), is(equalTo("Sun Prairie")));
        assertThat(actualOwner.getTelephone(), is(equalTo("6085551749")));
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when updating non-existent owner")
    void shouldThrowDataNotFoundExceptionWhenUpdatingNonExistentOwner() {
        // Given: No owner exists with the given ID
        final UpdateOwnerRequest request = new UpdateOwnerRequest();
        request.setFirstName("Betty");
        request.setLastName("Davis");
        request.setAddress(VALID_ADDRESS);
        request.setCity(VALID_CITY);
        request.setTelephone(VALID_TELEPHONE);
        given(ownerRepository.findById(NON_EXISTENT_OWNER_ID)).willReturn(Optional.empty());

        // When & Then: Updating non-existent owner should throw DataNotFoundException
        final DataNotFoundException exception = assertThrows(
            DataNotFoundException.class,
            () -> ownerService.update(NON_EXISTENT_OWNER_ID, request)
        );

        assertThat(exception.getMessage(), is(equalTo("The requested owner does not exist.")));
    }

    @Test
    @DisplayName("Should delete owner when valid ID exists")
    void shouldDeleteOwnerWhenValidIdExists() {
        // Given: An owner with valid ID exists in the repository
        final Owner existingOwner = aValidOwner();
        given(ownerRepository.findById(VALID_OWNER_ID)).willReturn(Optional.of(existingOwner));

        // When: Deleting the owner
        ownerService.delete(VALID_OWNER_ID);

        // Then: The owner should be deleted from the repository
        verify(ownerRepository).deleteById(VALID_OWNER_ID);
    }

    @Test
    @DisplayName("Should throw DataNotFoundException when deleting non-existent owner")
    void shouldThrowDataNotFoundExceptionWhenDeletingNonExistentOwner() {
        // Given: No owner exists with the given ID
        given(ownerRepository.findById(NON_EXISTENT_OWNER_ID)).willReturn(Optional.empty());

        // When & Then: Deleting non-existent owner should throw DataNotFoundException
        final DataNotFoundException exception = assertThrows(
            DataNotFoundException.class,
            () -> ownerService.delete(NON_EXISTENT_OWNER_ID)
        );

        assertThat(exception.getMessage(), is(equalTo("The requested owner does not exist.")));
    }

    @Test
    @DisplayName("Should return owners by last name when last name provided")
    void shouldReturnOwnersByLastNameWhenLastNameProvided() {
        // Given: One owner with last name Franklin exists in the repository
        final Owner expectedOwner = aValidOwner();
        given(ownerRepository.findByLastName("Franklin")).willReturn(List.of(expectedOwner));

        // When: Finding owners by last name
        final List<Owner> actualOwners = ownerService.findByLastName("Franklin");

        // Then: The list should contain one owner with last name Franklin
        assertThat(actualOwners, is(notNullValue()));
        assertThat(actualOwners, hasSize(1));
        assertThat(actualOwners.get(0).getLastName(), is(equalTo("Franklin")));
    }

    @Test
    @DisplayName("Should return empty list when no owners match last name")
    void shouldReturnEmptyListWhenNoOwnersMatchLastName() {
        // Given: No owners exist with the given last name
        given(ownerRepository.findByLastName("Nonexistent")).willReturn(Collections.emptyList());

        // When: Finding owners by non-existent last name
        final List<Owner> actualOwners = ownerService.findByLastName("Nonexistent");

        // Then: An empty list should be returned
        assertThat(actualOwners, is(notNullValue()));
        assertThat(actualOwners, is(empty()));
    }

    @Test
    @DisplayName("Should return all owners when last name is null")
    void shouldReturnAllOwnersWhenLastNameIsNull() {
        // Given: Multiple owners exist in the repository
        final Owner firstOwner = aValidOwner();
        final Owner secondOwner = aValidOwner();
        secondOwner.setId(2);
        secondOwner.setFirstName("Betty");
        secondOwner.setLastName("Davis");
        final List<Owner> expectedOwners = List.of(firstOwner, secondOwner);
        given(ownerRepository.findAll()).willReturn(expectedOwners);

        // When: Finding owners with null last name
        final List<Owner> actualOwners = ownerService.findByLastName(null);

        // Then: All owners should be returned
        assertThat(actualOwners, is(notNullValue()));
        assertThat(actualOwners, hasSize(2));
    }

}
