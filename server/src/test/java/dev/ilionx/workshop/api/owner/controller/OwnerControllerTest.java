package dev.ilionx.workshop.api.owner.controller;

import dev.ilionx.workshop.api.owner.model.Owner;
import dev.ilionx.workshop.api.owner.model.request.CreateOwnerRequest;
import dev.ilionx.workshop.api.owner.model.request.UpdateOwnerRequest;
import dev.ilionx.workshop.api.owner.model.response.OwnerResponse;
import dev.ilionx.workshop.support.IntegrationTest;
import io.github.jframe.exception.resource.ErrorResponseResource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static dev.ilionx.workshop.api.Paths.OWNERS;
import static dev.ilionx.workshop.api.Paths.OWNER_BY_ID;
import static dev.ilionx.workshop.common.exception.ApiErrorCode.OWNER_NOT_FOUND;
import static io.github.jframe.util.mapper.ObjectMappers.fromJson;
import static io.github.jframe.util.mapper.ObjectMappers.toJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Integration Test - Owner Controller")
class OwnerControllerTest extends IntegrationTest {

    private static final int NON_EXISTENT_OWNER_ID = 999;

    private static final String OWNER_FIRST_NAME = "George";
    private static final String OWNER_LAST_NAME = "Franklin";
    private static final String OWNER_ADDRESS = "110 W. Liberty St.";
    private static final String OWNER_CITY = "Madison";
    private static final String OWNER_TELEPHONE = "6085551023";

    private static final String UPDATED_FIRST_NAME = "Betty";
    private static final String UPDATED_LAST_NAME = "Davis";

    // ========================= LIST =========================
    @Test
    @DisplayName("Should return all owners when owners exist")
    void shouldReturnAllOwnersWhenOwnersExist() throws Exception {
        // Given: Two owners exist in the database
        final Owner firstOwner = aSavedOwner();
        final Owner secondOwner = new Owner();
        secondOwner.setFirstName("Betty");
        secondOwner.setLastName("Davis");
        secondOwner.setAddress("638 Cardinal Ave.");
        secondOwner.setCity("Sun Prairie");
        secondOwner.setTelephone("6085551749");
        final Owner savedSecondOwner = ownerRepository.save(secondOwner);

        // When: Getting all owners
        // Then: Both owners should be returned with correct structure
        mockMvc.perform(get(OWNERS))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(equalTo(firstOwner.getId()))))
            .andExpect(jsonPath("$[0].firstName", is(equalTo(OWNER_FIRST_NAME))))
            .andExpect(jsonPath("$[0].lastName", is(equalTo(OWNER_LAST_NAME))))
            .andExpect(jsonPath("$[0].address", is(equalTo(OWNER_ADDRESS))))
            .andExpect(jsonPath("$[0].city", is(equalTo(OWNER_CITY))))
            .andExpect(jsonPath("$[0].telephone", is(equalTo(OWNER_TELEPHONE))))
            .andExpect(jsonPath("$[1].id", is(equalTo(savedSecondOwner.getId()))))
            .andExpect(jsonPath("$[1].firstName", is(equalTo("Betty"))))
            .andExpect(jsonPath("$[1].lastName", is(equalTo("Davis"))));
    }

    @Test
    @DisplayName("Should return empty list when no owners exist")
    void shouldReturnEmptyListWhenNoOwnersExist() throws Exception {
        // Given: No owners exist in the database

        // When: Getting all owners
        // Then: Empty array should be returned
        mockMvc.perform(get(OWNERS))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(empty())));
    }

    @Test
    @DisplayName("Should return filtered owners when last name query param provided")
    void shouldReturnFilteredOwnersWhenLastNameQueryParamProvided() throws Exception {
        // Given: Two owners with different last names exist in the database
        aSavedOwner();
        final Owner davisOwner = new Owner();
        davisOwner.setFirstName("Betty");
        davisOwner.setLastName("Davis");
        davisOwner.setAddress("638 Cardinal Ave.");
        davisOwner.setCity("Sun Prairie");
        davisOwner.setTelephone("6085551749");
        ownerRepository.save(davisOwner);

        // When: Getting owners with lastName query parameter set to Franklin
        // Then: Only the Franklin owner should be returned
        mockMvc.perform(get(OWNERS).param("lastName", "Franklin"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].lastName", is(equalTo("Franklin"))));
    }

    @Test
    @DisplayName("Should return empty list when no owners match last name filter")
    void shouldReturnEmptyListWhenNoOwnersMatchLastNameFilter() throws Exception {
        // Given: One owner with last name Franklin exists in the database
        aSavedOwner();

        // When: Getting owners with lastName query parameter set to Nonexistent
        // Then: Empty array should be returned
        mockMvc.perform(get(OWNERS).param("lastName", "Nonexistent"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(empty())));
    }

    // ========================= GET BY ID =========================
    @Test
    @DisplayName("Should return owner when valid ID exists")
    void shouldReturnOwnerWhenValidIdExists() throws Exception {
        // Given: An owner exists in the database
        final Owner savedOwner = aSavedOwner();

        // When: Getting owner by ID
        // Then: Owner should be returned with all fields and empty pets list
        mockMvc.perform(get(OWNER_BY_ID, savedOwner.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(equalTo(savedOwner.getId()))))
            .andExpect(jsonPath("$.firstName", is(equalTo(OWNER_FIRST_NAME))))
            .andExpect(jsonPath("$.lastName", is(equalTo(OWNER_LAST_NAME))))
            .andExpect(jsonPath("$.address", is(equalTo(OWNER_ADDRESS))))
            .andExpect(jsonPath("$.city", is(equalTo(OWNER_CITY))))
            .andExpect(jsonPath("$.telephone", is(equalTo(OWNER_TELEPHONE))))
            .andExpect(jsonPath("$.pets", is(empty())));
    }

    @Test
    @DisplayName("Should return not found when owner ID does not exist")
    void shouldReturnNotFoundWhenOwnerIdDoesNotExist() throws Exception {
        // Given: No owner exists with ID 999

        // When: Getting owner by non-existent ID
        final ResultActions response = mockMvc.perform(get(OWNER_BY_ID, NON_EXISTENT_OWNER_ID));

        // Then: HTTP 404 Not Found should be returned with error message
        response.andExpect(status().isNotFound());

        final String content = response.andReturn().getResponse().getContentAsString();
        final ErrorResponseResource error = fromJson(content, ErrorResponseResource.class);
        assertThat(error.getErrorMessage(), is(equalTo(OWNER_NOT_FOUND.getReason())));
    }

    // ========================= CREATE =========================
    @Test
    @DisplayName("Should create owner when valid data provided")
    void shouldCreateOwnerWhenValidDataProvided() throws Exception {
        // Given: A valid create owner request
        final CreateOwnerRequest request = aCreateOwnerRequest();

        // When: Creating the owner via POST
        // Then: HTTP 201 Created should be returned with owner response including generated ID
        mockMvc.perform(
            post(OWNERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(notNullValue())))
            .andExpect(jsonPath("$.firstName", is(equalTo(OWNER_FIRST_NAME))))
            .andExpect(jsonPath("$.lastName", is(equalTo(OWNER_LAST_NAME))))
            .andExpect(jsonPath("$.address", is(equalTo(OWNER_ADDRESS))))
            .andExpect(jsonPath("$.city", is(equalTo(OWNER_CITY))))
            .andExpect(jsonPath("$.telephone", is(equalTo(OWNER_TELEPHONE))))
            .andExpect(jsonPath("$.pets", is(empty())));
    }

    @Test
    @DisplayName("Should return owner when getting created owner")
    void shouldReturnOwnerWhenGettingCreatedOwner() throws Exception {
        // Given: A create owner request
        final CreateOwnerRequest request = aCreateOwnerRequest();

        // When: Creating the owner
        final String createResponse = mockMvc.perform(
            post(OWNERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        )
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        final Integer createdOwnerId = fromJson(createResponse, OwnerResponse.class).getId();

        // Then: Getting the created owner should return matching data
        mockMvc.perform(get(OWNER_BY_ID, createdOwnerId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(equalTo(createdOwnerId))))
            .andExpect(jsonPath("$.firstName", is(equalTo(OWNER_FIRST_NAME))))
            .andExpect(jsonPath("$.lastName", is(equalTo(OWNER_LAST_NAME))))
            .andExpect(jsonPath("$.address", is(equalTo(OWNER_ADDRESS))))
            .andExpect(jsonPath("$.city", is(equalTo(OWNER_CITY))))
            .andExpect(jsonPath("$.telephone", is(equalTo(OWNER_TELEPHONE))));
    }

    // ========================= UPDATE =========================
    @Test
    @DisplayName("Should update owner when valid data provided")
    void shouldUpdateOwnerWhenValidDataProvided() throws Exception {
        // Given: An existing owner in the database
        final Owner savedOwner = aSavedOwner();
        final UpdateOwnerRequest request = anUpdateOwnerRequest();

        // When: Updating the owner via PUT
        // Then: HTTP 200 OK should be returned with updated owner response
        mockMvc.perform(
            put(OWNER_BY_ID, savedOwner.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(equalTo(savedOwner.getId()))))
            .andExpect(jsonPath("$.firstName", is(equalTo(UPDATED_FIRST_NAME))))
            .andExpect(jsonPath("$.lastName", is(equalTo(UPDATED_LAST_NAME))));
    }

    @Test
    @DisplayName("Should return not found when updating non-existent owner")
    void shouldReturnNotFoundWhenUpdatingNonExistentOwner() throws Exception {
        // Given: No owner exists with ID 999
        final UpdateOwnerRequest request = anUpdateOwnerRequest();

        // When: Updating non-existent owner via PUT
        final ResultActions response = mockMvc.perform(
            put(OWNER_BY_ID, NON_EXISTENT_OWNER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        );

        // Then: HTTP 404 Not Found should be returned with error message
        response.andExpect(status().isNotFound());

        final String content = response.andReturn().getResponse().getContentAsString();
        final ErrorResponseResource error = fromJson(content, ErrorResponseResource.class);
        assertThat(error.getErrorMessage(), is(equalTo(OWNER_NOT_FOUND.getReason())));
    }

    // ========================= DELETE =========================
    @Test
    @DisplayName("Should delete owner when valid ID exists")
    void shouldDeleteOwnerWhenValidIdExists() throws Exception {
        // Given: An existing owner in the database
        final Owner savedOwner = aSavedOwner();

        // When: Deleting the owner via DELETE
        // Then: HTTP 204 No Content should be returned
        mockMvc.perform(delete(OWNER_BY_ID, savedOwner.getId()))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return not found when deleting non-existent owner")
    void shouldReturnNotFoundWhenDeletingNonExistentOwner() throws Exception {
        // Given: No owner exists with ID 999

        // When: Deleting non-existent owner via DELETE
        final ResultActions response = mockMvc.perform(delete(OWNER_BY_ID, NON_EXISTENT_OWNER_ID));

        // Then: HTTP 404 Not Found should be returned with error message
        response.andExpect(status().isNotFound());

        final String content = response.andReturn().getResponse().getContentAsString();
        final ErrorResponseResource error = fromJson(content, ErrorResponseResource.class);
        assertThat(error.getErrorMessage(), is(equalTo(OWNER_NOT_FOUND.getReason())));
    }
}
