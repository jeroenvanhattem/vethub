package dev.ilionx.workshop.api.vet.service;

import dev.ilionx.workshop.api.vet.model.Specialty;
import dev.ilionx.workshop.api.vet.model.request.CreateSpecialtyRequest;
import dev.ilionx.workshop.api.vet.model.request.UpdateSpecialtyRequest;
import dev.ilionx.workshop.api.vet.repository.SpecialtyRepository;
import io.github.jframe.exception.core.DataNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static dev.ilionx.workshop.common.exception.ApiErrorCode.SPECIALTY_NOT_FOUND;

/**
 * Service for managing veterinary specialty operations.
 */
@Service
@RequiredArgsConstructor
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    /**
     * Retrieves all specialties.
     *
     * @return list of all specialties
     */
    @Transactional(readOnly = true)
    public List<Specialty> findAll() {
        return specialtyRepository.findAll();
    }

    /**
     * Finds a specialty by ID.
     *
     * @param id the specialty ID
     * @return the found specialty
     */
    @Transactional(readOnly = true)
    public Specialty findById(final Integer id) {
        return specialtyRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException(SPECIALTY_NOT_FOUND));
    }

    /**
     * Creates a new specialty.
     *
     * @param request the create request
     * @return the created specialty
     */
    @Transactional
    public Specialty create(final CreateSpecialtyRequest request) {
        final Specialty specialty = new Specialty();
        specialty.setName(request.getName());
        return specialtyRepository.save(specialty);
    }

    /**
     * Updates an existing specialty.
     *
     * @param id      the specialty ID
     * @param request the update request
     * @return the updated specialty
     */
    @Transactional
    public Specialty update(final Integer id, final UpdateSpecialtyRequest request) {
        final Specialty specialty = findById(id);
        specialty.setName(request.getName());
        return specialtyRepository.save(specialty);
    }

    /**
     * Deletes a specialty by ID.
     *
     * @param id the specialty ID
     */
    @Transactional
    public void delete(final Integer id) {
        findById(id);
        specialtyRepository.deleteById(id);
    }
}
