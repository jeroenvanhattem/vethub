package dev.ilionx.workshop.api.pet.controller;

import dev.ilionx.workshop.api.pet.model.PetType;
import dev.ilionx.workshop.api.pet.model.mapper.PetTypeMapper;
import dev.ilionx.workshop.api.pet.model.request.CreatePetTypeRequest;
import dev.ilionx.workshop.api.pet.model.request.UpdatePetTypeRequest;
import dev.ilionx.workshop.api.pet.model.response.PetTypeResponse;
import dev.ilionx.workshop.api.pet.service.PetTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dev.ilionx.workshop.api.Paths.PET_TYPES;
import static dev.ilionx.workshop.api.Paths.PET_TYPE_BY_ID;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for managing pet types.
 */
@Tag(
    name = "Pet Type",
    description = "Pet type management endpoints"
)
@RestController
@RequiredArgsConstructor
public class PetTypeController {

    private final PetTypeService petTypeService;
    private final PetTypeMapper petTypeMapper;

    @ResponseStatus(OK)
    @Operation(
        summary = "Get all pet types",
        description = "Returns a list of all pet types"
    )
    @GetMapping(
        path = PET_TYPES,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<PetTypeResponse>> getAllPetTypes() {
        final List<PetType> petTypes = petTypeService.findAll();
        return ResponseEntity.status(OK).body(petTypeMapper.toResponseList(petTypes));
    }

    @ResponseStatus(OK)
    @Operation(
        summary = "Get pet type by ID",
        description = "Returns a single pet type by its unique identifier"
    )
    @GetMapping(
        path = PET_TYPE_BY_ID,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PetTypeResponse> getPetTypeById(@PathVariable final Integer id) {
        final PetType petType = petTypeService.findById(id);
        return ResponseEntity.status(OK).body(petTypeMapper.toResponse(petType));
    }

    @ResponseStatus(CREATED)
    @Operation(
        summary = "Create pet type",
        description = "Creates a new pet type"
    )
    @PostMapping(
        path = PET_TYPES,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PetTypeResponse> createPetType(@RequestBody final CreatePetTypeRequest request) {
        final PetType petType = petTypeService.create(request);
        return ResponseEntity.status(CREATED).body(petTypeMapper.toResponse(petType));
    }

    @ResponseStatus(OK)
    @Operation(
        summary = "Update pet type",
        description = "Updates an existing pet type"
    )
    @PutMapping(
        path = PET_TYPE_BY_ID,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PetTypeResponse> updatePetType(@PathVariable final Integer id, @RequestBody final UpdatePetTypeRequest request) {
        final PetType petType = petTypeService.update(id, request);
        return ResponseEntity.status(OK).body(petTypeMapper.toResponse(petType));
    }

    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Delete pet type",
        description = "Deletes a pet type by its unique identifier"
    )
    @DeleteMapping(path = PET_TYPE_BY_ID)
    public ResponseEntity<Void> deletePetType(@PathVariable final Integer id) {
        petTypeService.delete(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
