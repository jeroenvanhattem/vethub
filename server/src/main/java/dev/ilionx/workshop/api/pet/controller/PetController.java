package dev.ilionx.workshop.api.pet.controller;

import dev.ilionx.workshop.api.pet.model.Pet;
import dev.ilionx.workshop.api.pet.model.mapper.PetMapper;
import dev.ilionx.workshop.api.pet.model.request.CreatePetRequest;
import dev.ilionx.workshop.api.pet.model.request.UpdatePetRequest;
import dev.ilionx.workshop.api.pet.model.response.PetResponse;
import dev.ilionx.workshop.api.pet.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dev.ilionx.workshop.api.Paths.OWNER_PETS;
import static dev.ilionx.workshop.api.Paths.OWNER_PET_BY_ID;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for managing pets nested under owners.
 */
@Tag(
    name = "Owner Pet",
    description = "Pet management endpoints nested under owners"
)
@RestController
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    private final PetMapper petMapper;

    @ResponseStatus(OK)
    @Operation(
        summary = "Get pets by owner",
        description = "Returns all pets belonging to a specific owner"
    )
    @GetMapping(
        path = OWNER_PETS,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<PetResponse>> getPetsByOwner(@PathVariable final Integer ownerId) {
        final List<Pet> pets = petService.findByOwnerId(ownerId);
        return ResponseEntity.status(OK).body(petMapper.toResponseList(pets));
    }

    @ResponseStatus(OK)
    @Operation(
        summary = "Get pet by ID",
        description = "Returns a single pet by owner and pet identifiers"
    )
    @GetMapping(
        path = OWNER_PET_BY_ID,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PetResponse> getPetById(
        @PathVariable final Integer ownerId,
        @PathVariable final Integer petId
    ) {
        final Pet pet = petService.findByIdAndOwnerId(petId, ownerId);
        return ResponseEntity.status(OK).body(petMapper.toResponse(pet));
    }

    @ResponseStatus(CREATED)
    @Operation(
        summary = "Create pet",
        description = "Creates a new pet for a specific owner"
    )
    @PostMapping(
        path = OWNER_PETS,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PetResponse> createPet(
        @PathVariable final Integer ownerId,
        @RequestBody final CreatePetRequest request
    ) {
        final Pet pet = petService.create(ownerId, request);
        return ResponseEntity.status(CREATED).body(petMapper.toResponse(pet));
    }

    @ResponseStatus(OK)
    @Operation(
        summary = "Update pet",
        description = "Updates an existing pet for a specific owner"
    )
    @PutMapping(
        path = OWNER_PET_BY_ID,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PetResponse> updatePet(
        @PathVariable final Integer ownerId,
        @PathVariable final Integer petId,
        @RequestBody final UpdatePetRequest request
    ) {
        petService.findByIdAndOwnerId(petId, ownerId);
        final Pet pet = petService.update(petId, request);
        return ResponseEntity.status(OK).body(petMapper.toResponse(pet));
    }

    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Delete pet",
        description = "Deletes a pet for a specific owner"
    )
    @DeleteMapping(path = OWNER_PET_BY_ID)
    public ResponseEntity<Void> deletePet(
        @PathVariable final Integer ownerId,
        @PathVariable final Integer petId
    ) {
        petService.findByIdAndOwnerId(petId, ownerId);
        petService.delete(petId);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
