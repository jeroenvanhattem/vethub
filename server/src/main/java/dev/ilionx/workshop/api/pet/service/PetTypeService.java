package dev.ilionx.workshop.api.pet.service;

import dev.ilionx.workshop.api.pet.model.PetType;
import dev.ilionx.workshop.api.pet.model.request.CreatePetTypeRequest;
import dev.ilionx.workshop.api.pet.model.request.UpdatePetTypeRequest;
import dev.ilionx.workshop.api.pet.repository.PetTypeRepository;
import io.github.jframe.exception.core.DataNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static dev.ilionx.workshop.common.exception.ApiErrorCode.PET_TYPE_NOT_FOUND;

/**
 * Service for managing pet types (e.g., Cat, Dog, Bird).
 */
@Service
@RequiredArgsConstructor
public class PetTypeService {

    private final PetTypeRepository petTypeRepository;

    /**
     * Retrieves all available pet types.
     *
     * @return list of all pet types
     */
    @Transactional(readOnly = true)
    public List<PetType> findAll() {
        return petTypeRepository.findAll();
    }

    /**
     * Finds a pet type by its unique identifier.
     *
     * @param id the pet type's unique identifier
     * @return the pet type entity
     */
    @Transactional(readOnly = true)
    public PetType findById(final Integer id) {
        return petTypeRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException(PET_TYPE_NOT_FOUND));
    }

    /**
     * Creates a new pet type.
     *
     * @param request the creation request containing the pet type name
     * @return the created pet type entity
     */
    @Transactional
    public PetType create(final CreatePetTypeRequest request) {
        final PetType petType = new PetType();
        petType.setName(request.getName());
        return petTypeRepository.save(petType);
    }

    /**
     * Updates an existing pet type.
     *
     * @param id      the pet type's unique identifier
     * @param request the update request containing the new name
     * @return the updated pet type entity
     */
    @Transactional
    public PetType update(final Integer id, final UpdatePetTypeRequest request) {
        final PetType petType = findById(id);
        petType.setName(request.getName());
        return petTypeRepository.save(petType);
    }

    /**
     * Deletes a pet type by its unique identifier.
     *
     * @param id the pet type's unique identifier
     */
    @Transactional
    public void delete(final Integer id) {
        findById(id);
        petTypeRepository.deleteById(id);
    }
}
