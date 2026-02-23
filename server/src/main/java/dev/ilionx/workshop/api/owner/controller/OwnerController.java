package dev.ilionx.workshop.api.owner.controller;

import dev.ilionx.workshop.api.owner.model.Owner;
import dev.ilionx.workshop.api.owner.model.mapper.OwnerMapper;
import dev.ilionx.workshop.api.owner.model.request.CreateOwnerRequest;
import dev.ilionx.workshop.api.owner.model.request.UpdateOwnerRequest;
import dev.ilionx.workshop.api.owner.model.response.OwnerResponse;
import dev.ilionx.workshop.api.owner.model.validator.OwnerValidator;
import dev.ilionx.workshop.api.owner.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dev.ilionx.workshop.api.Paths.OWNERS;
import static dev.ilionx.workshop.api.Paths.OWNER_BY_ID;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for managing pet owners.
 */
@Tag(
    name = "Owner",
    description = "Owner management endpoints"
)
@RestController
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;
    private final OwnerMapper ownerMapper;
    private final OwnerValidator ownerValidator;

    @ResponseStatus(OK)
    @Operation(
        summary = "Get all owners",
        description = "Returns a list of all pet owners, optionally filtered by last name"
    )
    @GetMapping(
        path = OWNERS,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<OwnerResponse>> getAllOwners(@RequestParam(required = false) final String lastName) {
        final List<Owner> owners = ownerService.findByLastName(lastName);
        return ResponseEntity.status(OK).body(ownerMapper.toResponseList(owners));
    }

    @ResponseStatus(OK)
    @Operation(
        summary = "Get owner by ID",
        description = "Returns a single owner by their unique identifier"
    )
    @GetMapping(
        path = OWNER_BY_ID,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<OwnerResponse> getOwnerById(@PathVariable final Integer id) {
        final Owner owner = ownerService.findById(id);
        return ResponseEntity.status(OK).body(ownerMapper.toResponse(owner));
    }

    @ResponseStatus(CREATED)
    @Operation(
        summary = "Create owner",
        description = "Creates a new pet owner"
    )
    @PostMapping(
        path = OWNERS,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<OwnerResponse> createOwner(@RequestBody final CreateOwnerRequest request) {
        ownerValidator.validateAndThrow(request);
        final Owner owner = ownerService.create(request);
        return ResponseEntity.status(CREATED).body(ownerMapper.toResponse(owner));
    }

    @ResponseStatus(OK)
    @Operation(
        summary = "Update owner",
        description = "Updates an existing pet owner"
    )
    @PutMapping(
        path = OWNER_BY_ID,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<OwnerResponse> updateOwner(@PathVariable final Integer id, @RequestBody final UpdateOwnerRequest request) {
        ownerValidator.validateAndThrow(request);
        final Owner owner = ownerService.update(id, request);
        return ResponseEntity.status(OK).body(ownerMapper.toResponse(owner));
    }

    @ResponseStatus(NO_CONTENT)
    @Operation(
        summary = "Delete owner",
        description = "Deletes a pet owner by their unique identifier"
    )
    @DeleteMapping(path = OWNER_BY_ID)
    public ResponseEntity<Void> deleteOwner(@PathVariable final Integer id) {
        ownerService.delete(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
