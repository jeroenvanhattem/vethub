package dev.ilionx.workshop.api.vaccination.controller;

import dev.ilionx.workshop.api.pet.service.PetService;
import dev.ilionx.workshop.api.vaccination.model.Vaccination;
import dev.ilionx.workshop.api.vaccination.model.mapper.VaccinationMapper;
import dev.ilionx.workshop.api.vaccination.model.request.CreateVaccinationRequest;
import dev.ilionx.workshop.api.vaccination.model.request.UpdateVaccinationRequest;
import dev.ilionx.workshop.api.vaccination.model.response.VaccinationResponse;
import dev.ilionx.workshop.api.vaccination.service.VaccinationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dev.ilionx.workshop.api.Paths.PET_VACCINATIONS;
import static dev.ilionx.workshop.api.Paths.PET_VACCINATION_BY_ID;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for managing vaccinations nested under owner/pet.
 */
@Tag(
    name = "Vaccination",
    description = "Vaccination management endpoints (nested under owner/pet)"
)
@RestController
@RequiredArgsConstructor
public class VaccinationController {

    private final VaccinationService vaccinationService;
    private final PetService petService;
    private final VaccinationMapper vaccinationMapper;

    @ResponseStatus(OK)
    @Operation(
        summary = "Get vaccinations by pet",
        description = "Returns all vaccinations for a specific pet owned by a specific owner"
    )
    @GetMapping(
        path = PET_VACCINATIONS,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<VaccinationResponse>> getVaccinationsByPet(
        @PathVariable final Integer ownerId,
        @PathVariable final Integer petId
    ) {
        petService.findByIdAndOwnerId(petId, ownerId);
        final List<Vaccination> vaccinations = vaccinationService.findByPetId(petId);
        return ResponseEntity.status(OK).body(vaccinationMapper.toResponseList(vaccinations));
    }

    @ResponseStatus(CREATED)
    @Operation(
        summary = "Create vaccination",
        description = "Creates a new vaccination for a specific pet owned by a specific owner"
    )
    @PostMapping(
        path = PET_VACCINATIONS,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<VaccinationResponse> createVaccination(
        @PathVariable final Integer ownerId,
        @PathVariable final Integer petId,
        @Valid
        @RequestBody final CreateVaccinationRequest request
    ) {
        petService.findByIdAndOwnerId(petId, ownerId);
        final Vaccination createdVaccination = vaccinationService.create(petId, request);
        return ResponseEntity.status(CREATED).body(vaccinationMapper.toResponse(createdVaccination));
    }

    @ResponseStatus(OK)
    @Operation(
        summary = "Get vaccination by ID",
        description = "Returns a single vaccination for a specific pet owned by a specific owner"
    )
    @GetMapping(
        path = PET_VACCINATION_BY_ID,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<VaccinationResponse> getVaccinationById(
        @PathVariable final Integer ownerId,
        @PathVariable final Integer petId,
        @PathVariable final Integer vaccinationId
    ) {
        petService.findByIdAndOwnerId(petId, ownerId);
        final Vaccination vaccination = vaccinationService.findById(vaccinationId);
        return ResponseEntity.status(OK).body(vaccinationMapper.toResponse(vaccination));
    }

    @ResponseStatus(OK)
    @Operation(
        summary = "Update vaccination",
        description = "Updates an existing vaccination for a specific pet owned by a specific owner"
    )
    @PutMapping(
        path = PET_VACCINATION_BY_ID,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<VaccinationResponse> updateVaccination(
        @PathVariable final Integer ownerId,
        @PathVariable final Integer petId,
        @PathVariable final Integer vaccinationId,
        @Valid
        @RequestBody final UpdateVaccinationRequest request
    ) {
        petService.findByIdAndOwnerId(petId, ownerId);
        final Vaccination vaccination = vaccinationService.update(vaccinationId, request);
        return ResponseEntity.status(OK).body(vaccinationMapper.toResponse(vaccination));
    }

    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Delete vaccination",
        description = "Deletes a vaccination for a specific pet owned by a specific owner"
    )
    @DeleteMapping(path = PET_VACCINATION_BY_ID)
    public ResponseEntity<Void> deleteVaccination(
        @PathVariable final Integer ownerId,
        @PathVariable final Integer petId,
        @PathVariable final Integer vaccinationId
    ) {
        petService.findByIdAndOwnerId(petId, ownerId);
        vaccinationService.delete(vaccinationId);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
