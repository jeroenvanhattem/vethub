package dev.ilionx.workshop.api.pet.controller;

import dev.ilionx.workshop.api.pet.model.request.CreatePetTypeRequest;
import dev.ilionx.workshop.api.pet.model.request.UpdatePetTypeRequest;
import dev.ilionx.workshop.api.pet.model.response.PetTypeResponse;
import dev.ilionx.workshop.support.IntegrationTest;
import io.github.jframe.exception.resource.ErrorResponseResource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static dev.ilionx.workshop.api.Paths.PET_TYPES;
import static dev.ilionx.workshop.api.Paths.PET_TYPE_BY_ID;
import static dev.ilionx.workshop.common.exception.ApiErrorCode.PET_TYPE_NOT_FOUND;
import static io.github.jframe.util.mapper.ObjectMappers.fromJson;
import static io.github.jframe.util.mapper.ObjectMappers.toJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Integration Test - Pet Type Controller")
class PetTypeControllerTest extends IntegrationTest {

    private static final int EXPECTED_PET_TYPE_COUNT = 6;
    private static final int NON_EXISTENT_PET_TYPE_ID = 999;

    private static final String PET_TYPE_NAME = "Rabbit";
    private static final String UPDATED_PET_TYPE_NAME = "Bunny";

    // ========================= LIST =========================
    @Test
    @DisplayName("Should return all pet types when pet types exist")
    void shouldReturnAllPetTypesWhenPetTypesExist() throws Exception {
        // Given: Seed data has 6 pet types (Cat, Dog, Lizard, Snake, Bird, Hamster)

        // When: Getting all pet types
        // Then: All 6 pet types should be returned with correct structure
        mockMvc.perform(get(PET_TYPES))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(EXPECTED_PET_TYPE_COUNT)))
            .andExpect(jsonPath("$[0].id", is(equalTo(1))))
            .andExpect(jsonPath("$[0].name", is(equalTo("Cat"))))
            .andExpect(jsonPath("$[1].id", is(equalTo(2))))
            .andExpect(jsonPath("$[1].name", is(equalTo("Dog"))))
            .andExpect(jsonPath("$[2].id", is(equalTo(3))))
            .andExpect(jsonPath("$[2].name", is(equalTo("Lizard"))))
            .andExpect(jsonPath("$[3].id", is(equalTo(4))))
            .andExpect(jsonPath("$[3].name", is(equalTo("Snake"))))
            .andExpect(jsonPath("$[4].id", is(equalTo(5))))
            .andExpect(jsonPath("$[4].name", is(equalTo("Bird"))))
            .andExpect(jsonPath("$[5].id", is(equalTo(6))))
            .andExpect(jsonPath("$[5].name", is(equalTo("Hamster"))));
    }

    @Test
    @DisplayName("Should return OK status when getting pet types")
    void shouldReturnOkStatusWhenGettingPetTypes() throws Exception {
        // Given: Pet types exist in the database

        // When: Getting all pet types
        // Then: HTTP 200 OK should be returned
        mockMvc.perform(get(PET_TYPES))
            .andExpect(status().isOk());
    }

    // ========================= GET BY ID =========================
    @Test
    @DisplayName("Should return pet type when valid ID exists")
    void shouldReturnPetTypeWhenValidIdExists() throws Exception {
        // Given: Seed data has pet type with ID 1 (Cat)

        // When: Getting pet type by ID 1
        // Then: Pet type should be returned with id=1 and name="Cat"
        mockMvc.perform(get(PET_TYPE_BY_ID, 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(equalTo(1))))
            .andExpect(jsonPath("$.name", is(equalTo("Cat"))));
    }

    @Test
    @DisplayName("Should return not found when pet type ID does not exist")
    void shouldReturnNotFoundWhenPetTypeIdDoesNotExist() throws Exception {
        // Given: No pet type exists with ID 999

        // When: Getting pet type by non-existent ID
        final ResultActions response = mockMvc.perform(get(PET_TYPE_BY_ID, NON_EXISTENT_PET_TYPE_ID));

        // Then: HTTP 404 Not Found should be returned with error message
        response.andExpect(status().isNotFound());

        final String content = response.andReturn().getResponse().getContentAsString();
        final ErrorResponseResource error = fromJson(content, ErrorResponseResource.class);
        assertThat(error.getErrorMessage(), is(equalTo(PET_TYPE_NOT_FOUND.getReason())));
    }

    // ========================= CREATE =========================
    @Test
    @DisplayName("Should create pet type when valid data provided")
    void shouldCreatePetTypeWhenValidDataProvided() throws Exception {
        // Given: A valid create pet type request
        final CreatePetTypeRequest request = aCreatePetTypeRequest();

        // When: Creating the pet type via POST
        // Then: HTTP 201 Created should be returned with pet type response including generated ID
        mockMvc.perform(
            post(PET_TYPES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(notNullValue())))
            .andExpect(jsonPath("$.name", is(equalTo(PET_TYPE_NAME))));
    }

    // ========================= UPDATE =========================
    @Test
    @DisplayName("Should update pet type when valid data provided")
    void shouldUpdatePetTypeWhenValidDataProvided() throws Exception {
        // Given: A newly created pet type via POST
        final String createResponse = mockMvc.perform(
            post(PET_TYPES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(aCreatePetTypeRequest()))
        )
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        final Integer createdPetTypeId = fromJson(createResponse, PetTypeResponse.class).getId();

        // When: Updating the pet type via PUT
        final UpdatePetTypeRequest request = anUpdatePetTypeRequest();

        // Then: HTTP 200 OK should be returned with updated pet type response
        mockMvc.perform(
            put(PET_TYPE_BY_ID, createdPetTypeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(equalTo(createdPetTypeId))))
            .andExpect(jsonPath("$.name", is(equalTo(UPDATED_PET_TYPE_NAME))));
    }

    @Test
    @DisplayName("Should return not found when updating non-existent pet type")
    void shouldReturnNotFoundWhenUpdatingNonExistentPetType() throws Exception {
        // Given: No pet type exists with ID 999
        final UpdatePetTypeRequest request = anUpdatePetTypeRequest();

        // When: Updating non-existent pet type via PUT
        final ResultActions response = mockMvc.perform(
            put(PET_TYPE_BY_ID, NON_EXISTENT_PET_TYPE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        );

        // Then: HTTP 404 Not Found should be returned with error message
        response.andExpect(status().isNotFound());

        final String content = response.andReturn().getResponse().getContentAsString();
        final ErrorResponseResource error = fromJson(content, ErrorResponseResource.class);
        assertThat(error.getErrorMessage(), is(equalTo(PET_TYPE_NOT_FOUND.getReason())));
    }

    // ========================= DELETE =========================
    @Test
    @DisplayName("Should delete pet type when valid ID exists")
    void shouldDeletePetTypeWhenValidIdExists() throws Exception {
        // Given: A newly created pet type via POST
        final String createResponse = mockMvc.perform(
            post(PET_TYPES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(aCreatePetTypeRequest()))
        )
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        final Integer createdPetTypeId = fromJson(createResponse, PetTypeResponse.class).getId();

        // When: Deleting the pet type via DELETE
        // Then: HTTP 204 No Content should be returned
        mockMvc.perform(delete(PET_TYPE_BY_ID, createdPetTypeId))
            .andExpect(status().isNoContent());

        // And: Getting the deleted pet type should return HTTP 404
        mockMvc.perform(get(PET_TYPE_BY_ID, createdPetTypeId))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return not found when deleting non-existent pet type")
    void shouldReturnNotFoundWhenDeletingNonExistentPetType() throws Exception {
        // Given: No pet type exists with ID 999

        // When: Deleting non-existent pet type via DELETE
        final ResultActions response = mockMvc.perform(delete(PET_TYPE_BY_ID, NON_EXISTENT_PET_TYPE_ID));

        // Then: HTTP 404 Not Found should be returned with error message
        response.andExpect(status().isNotFound());

        final String content = response.andReturn().getResponse().getContentAsString();
        final ErrorResponseResource error = fromJson(content, ErrorResponseResource.class);
        assertThat(error.getErrorMessage(), is(equalTo(PET_TYPE_NOT_FOUND.getReason())));
    }
}
