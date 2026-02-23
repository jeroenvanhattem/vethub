package dev.ilionx.workshop.api.vet.controller;

import dev.ilionx.workshop.api.vet.model.request.CreateSpecialtyRequest;
import dev.ilionx.workshop.api.vet.model.request.UpdateSpecialtyRequest;
import dev.ilionx.workshop.api.vet.model.response.SpecialtyResponse;
import dev.ilionx.workshop.support.IntegrationTest;
import io.github.jframe.exception.resource.ErrorResponseResource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static dev.ilionx.workshop.api.Paths.SPECIALTIES;
import static dev.ilionx.workshop.api.Paths.SPECIALTY_BY_ID;
import static dev.ilionx.workshop.common.exception.ApiErrorCode.SPECIALTY_NOT_FOUND;
import static io.github.jframe.util.mapper.ObjectMappers.fromJson;
import static io.github.jframe.util.mapper.ObjectMappers.toJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Integration Test - Specialty Controller")
class SpecialtyControllerTest extends IntegrationTest {

    private static final int EXPECTED_SPECIALTY_COUNT = 3;
    private static final int NON_EXISTENT_SPECIALTY_ID = 999;

    private static final String SPECIALTY_NAME = "Dermatology";
    private static final String UPDATED_SPECIALTY_NAME = "Cardiology";

    // ========================= LIST =========================
    @Test
    @DisplayName("Should return all specialties when specialties exist")
    void shouldReturnAllSpecialtiesWhenSpecialtiesExist() throws Exception {
        // Given: Seed data has 3 specialties (Radiology, Surgery, Dentistry)

        // When: Getting all specialties
        // Then: All 3 specialties should be returned with correct structure
        mockMvc.perform(get(SPECIALTIES))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(EXPECTED_SPECIALTY_COUNT)))
            .andExpect(jsonPath("$[0].id", is(equalTo(1))))
            .andExpect(jsonPath("$[0].name", is(equalTo("Radiology"))))
            .andExpect(jsonPath("$[1].id", is(equalTo(2))))
            .andExpect(jsonPath("$[1].name", is(equalTo("Surgery"))))
            .andExpect(jsonPath("$[2].id", is(equalTo(3))))
            .andExpect(jsonPath("$[2].name", is(equalTo("Dentistry"))));
    }

    @Test
    @DisplayName("Should return OK status when getting specialties")
    void shouldReturnOkStatusWhenGettingSpecialties() throws Exception {
        // Given: Specialties exist in the database

        // When: Getting all specialties
        // Then: HTTP 200 OK should be returned
        mockMvc.perform(get(SPECIALTIES))
            .andExpect(status().isOk());
    }

    // ========================= GET BY ID =========================
    @Test
    @DisplayName("Should return specialty when valid ID exists")
    void shouldReturnSpecialtyWhenValidIdExists() throws Exception {
        // Given: Seed data has specialty with ID 1 (Radiology)

        // When: Getting specialty by ID 1
        // Then: Specialty should be returned with id=1 and name="Radiology"
        mockMvc.perform(get(SPECIALTY_BY_ID, 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(equalTo(1))))
            .andExpect(jsonPath("$.name", is(equalTo("Radiology"))));
    }

    @Test
    @DisplayName("Should return not found when specialty ID does not exist")
    void shouldReturnNotFoundWhenSpecialtyIdDoesNotExist() throws Exception {
        // Given: No specialty exists with ID 999

        // When: Getting specialty by non-existent ID
        final ResultActions response = mockMvc.perform(get(SPECIALTY_BY_ID, NON_EXISTENT_SPECIALTY_ID));

        // Then: HTTP 404 Not Found should be returned with error message
        response.andExpect(status().isNotFound());

        final String content = response.andReturn().getResponse().getContentAsString();
        final ErrorResponseResource error = fromJson(content, ErrorResponseResource.class);
        assertThat(error.getErrorMessage(), is(equalTo(SPECIALTY_NOT_FOUND.getReason())));
    }

    // ========================= CREATE =========================
    @Test
    @DisplayName("Should create specialty when valid data provided")
    void shouldCreateSpecialtyWhenValidDataProvided() throws Exception {
        // Given: A valid create specialty request
        final CreateSpecialtyRequest request = aCreateSpecialtyRequest();

        // When: Creating the specialty via POST
        // Then: HTTP 201 Created should be returned with specialty response including generated ID
        mockMvc.perform(
            post(SPECIALTIES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(notNullValue())))
            .andExpect(jsonPath("$.name", is(equalTo(SPECIALTY_NAME))));
    }

    // ========================= UPDATE =========================
    @Test
    @DisplayName("Should update specialty when valid data provided")
    void shouldUpdateSpecialtyWhenValidDataProvided() throws Exception {
        // Given: A newly created specialty via POST
        final String createResponse = mockMvc.perform(
            post(SPECIALTIES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(aCreateSpecialtyRequest()))
        )
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        final Integer createdSpecialtyId = fromJson(createResponse, SpecialtyResponse.class).getId();

        // When: Updating the specialty via PUT
        final UpdateSpecialtyRequest request = anUpdateSpecialtyRequest();

        // Then: HTTP 200 OK should be returned with updated specialty response
        mockMvc.perform(
            put(SPECIALTY_BY_ID, createdSpecialtyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(equalTo(createdSpecialtyId))))
            .andExpect(jsonPath("$.name", is(equalTo(UPDATED_SPECIALTY_NAME))));
    }

    @Test
    @DisplayName("Should return not found when updating non-existent specialty")
    void shouldReturnNotFoundWhenUpdatingNonExistentSpecialty() throws Exception {
        // Given: No specialty exists with ID 999
        final UpdateSpecialtyRequest request = anUpdateSpecialtyRequest();

        // When: Updating non-existent specialty via PUT
        final ResultActions response = mockMvc.perform(
            put(SPECIALTY_BY_ID, NON_EXISTENT_SPECIALTY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
        );

        // Then: HTTP 404 Not Found should be returned with error message
        response.andExpect(status().isNotFound());

        final String content = response.andReturn().getResponse().getContentAsString();
        final ErrorResponseResource error = fromJson(content, ErrorResponseResource.class);
        assertThat(error.getErrorMessage(), is(equalTo(SPECIALTY_NOT_FOUND.getReason())));
    }

    // ========================= DELETE =========================
    @Test
    @DisplayName("Should delete specialty when valid ID exists")
    void shouldDeleteSpecialtyWhenValidIdExists() throws Exception {
        // Given: A newly created specialty via POST
        final String createResponse = mockMvc.perform(
            post(SPECIALTIES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(aCreateSpecialtyRequest()))
        )
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        final Integer createdSpecialtyId = fromJson(createResponse, SpecialtyResponse.class).getId();

        // When: Deleting the specialty via DELETE
        // Then: HTTP 204 No Content should be returned
        mockMvc.perform(delete(SPECIALTY_BY_ID, createdSpecialtyId))
            .andExpect(status().isNoContent());

        // And: Getting the deleted specialty should return HTTP 404
        mockMvc.perform(get(SPECIALTY_BY_ID, createdSpecialtyId))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return not found when deleting non-existent specialty")
    void shouldReturnNotFoundWhenDeletingNonExistentSpecialty() throws Exception {
        // Given: No specialty exists with ID 999

        // When: Deleting non-existent specialty via DELETE
        final ResultActions response = mockMvc.perform(delete(SPECIALTY_BY_ID, NON_EXISTENT_SPECIALTY_ID));

        // Then: HTTP 404 Not Found should be returned with error message
        response.andExpect(status().isNotFound());

        final String content = response.andReturn().getResponse().getContentAsString();
        final ErrorResponseResource error = fromJson(content, ErrorResponseResource.class);
        assertThat(error.getErrorMessage(), is(equalTo(SPECIALTY_NOT_FOUND.getReason())));
    }
}
