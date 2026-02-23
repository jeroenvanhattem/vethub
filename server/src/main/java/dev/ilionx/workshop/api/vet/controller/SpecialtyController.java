package dev.ilionx.workshop.api.vet.controller;

import dev.ilionx.workshop.api.vet.model.Specialty;
import dev.ilionx.workshop.api.vet.model.mapper.SpecialtyMapper;
import dev.ilionx.workshop.api.vet.model.request.CreateSpecialtyRequest;
import dev.ilionx.workshop.api.vet.model.request.UpdateSpecialtyRequest;
import dev.ilionx.workshop.api.vet.model.response.SpecialtyResponse;
import dev.ilionx.workshop.api.vet.service.SpecialtyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dev.ilionx.workshop.api.Paths.SPECIALTIES;
import static dev.ilionx.workshop.api.Paths.SPECIALTY_BY_ID;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for managing veterinary specialties.
 */
@Tag(
    name = "Specialty",
    description = "Specialty management endpoints"
)
@RestController
@RequiredArgsConstructor
public class SpecialtyController {

    private final SpecialtyService specialtyService;
    private final SpecialtyMapper specialtyMapper;

    @ResponseStatus(OK)
    @Operation(
        summary = "Get all specialties",
        description = "Returns a list of all veterinary specialties"
    )
    @GetMapping(
        path = SPECIALTIES,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<SpecialtyResponse>> getAllSpecialties() {
        final List<Specialty> specialties = specialtyService.findAll();
        return ResponseEntity.status(OK).body(specialtyMapper.toResponseList(specialties));
    }

    @ResponseStatus(OK)
    @Operation(
        summary = "Get specialty by ID",
        description = "Returns a single specialty by its unique identifier"
    )
    @GetMapping(
        path = SPECIALTY_BY_ID,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SpecialtyResponse> getSpecialtyById(@PathVariable final Integer id) {
        final Specialty specialty = specialtyService.findById(id);
        return ResponseEntity.status(OK).body(specialtyMapper.toResponse(specialty));
    }

    @ResponseStatus(CREATED)
    @Operation(
        summary = "Create specialty",
        description = "Creates a new veterinary specialty"
    )
    @PostMapping(
        path = SPECIALTIES,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SpecialtyResponse> createSpecialty(@RequestBody final CreateSpecialtyRequest request) {
        final Specialty specialty = specialtyService.create(request);
        return ResponseEntity.status(CREATED).body(specialtyMapper.toResponse(specialty));
    }

    @ResponseStatus(OK)
    @Operation(
        summary = "Update specialty",
        description = "Updates an existing veterinary specialty"
    )
    @PutMapping(
        path = SPECIALTY_BY_ID,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SpecialtyResponse> updateSpecialty(@PathVariable final Integer id,
        @RequestBody final UpdateSpecialtyRequest request) {
        final Specialty specialty = specialtyService.update(id, request);
        return ResponseEntity.status(OK).body(specialtyMapper.toResponse(specialty));
    }

    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Delete specialty",
        description = "Deletes a specialty by its unique identifier"
    )
    @DeleteMapping(path = SPECIALTY_BY_ID)
    public ResponseEntity<Void> deleteSpecialty(@PathVariable final Integer id) {
        specialtyService.delete(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
