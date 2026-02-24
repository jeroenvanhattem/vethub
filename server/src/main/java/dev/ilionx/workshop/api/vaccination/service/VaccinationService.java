package dev.ilionx.workshop.api.vaccination.service;

import dev.ilionx.workshop.api.pet.model.Pet;
import dev.ilionx.workshop.api.pet.repository.PetRepository;
import dev.ilionx.workshop.api.vaccination.model.Vaccination;
import dev.ilionx.workshop.api.vaccination.model.request.CreateVaccinationRequest;
import dev.ilionx.workshop.api.vaccination.model.request.UpdateVaccinationRequest;
import dev.ilionx.workshop.api.vaccination.repository.VaccinationRepository;
import io.github.jframe.exception.core.DataNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing vaccinations.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class VaccinationService {

    private static final String PET_NOT_FOUND_MSG = "Pet not found with id: ";

    private final PetRepository petRepository;
    private final VaccinationRepository vaccinationRepository;

    /**
     * Find all vaccinations for a specific pet.
     *
     * @param petId the pet ID
     * @return list of vaccinations
     * @throws DataNotFoundException if pet not found
     */
    public List<Vaccination> findByPetId(final Integer petId) {
        petRepository.findById(petId)
            .orElseThrow(() -> new DataNotFoundException(PET_NOT_FOUND_MSG + petId));
        return vaccinationRepository.findByPetId(petId);
    }

    /**
     * Find vaccination by ID.
     *
     * @param id the vaccination ID
     * @return the vaccination
     * @throws DataNotFoundException if vaccination not found
     */
    public Vaccination findById(final Integer id) {
        return vaccinationRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Vaccination not found with id: " + id));
    }

    /**
     * Create a new vaccination.
     *
     * @param petId   the pet ID
     * @param request the create request
     * @return the created vaccination
     * @throws DataNotFoundException if pet not found
     */
    public Vaccination create(final Integer petId, final CreateVaccinationRequest request) {
        final Pet pet = petRepository.findById(petId)
            .orElseThrow(() -> new DataNotFoundException(PET_NOT_FOUND_MSG + petId));

        final Vaccination vaccination = new Vaccination();
        vaccination.setVaccineName(request.getVaccineName());
        vaccination.setVaccinationDate(request.getVaccinationDate());
        vaccination.setNextDueDate(request.getNextDueDate());
        vaccination.setPet(pet);

        return vaccinationRepository.save(vaccination);
    }

    /**
     * Update an existing vaccination.
     *
     * @param id      the vaccination ID
     * @param request the update request
     * @return the updated vaccination
     * @throws DataNotFoundException if vaccination not found
     */
    public Vaccination update(final Integer id, final UpdateVaccinationRequest request) {
        final Vaccination vaccination = findById(id);

        vaccination.setVaccineName(request.getVaccineName());
        vaccination.setVaccinationDate(request.getVaccinationDate());
        vaccination.setNextDueDate(request.getNextDueDate());

        return vaccinationRepository.save(vaccination);
    }

    /**
     * Delete a vaccination.
     *
     * @param id the vaccination ID
     * @throws DataNotFoundException if vaccination not found
     */
    public void delete(final Integer id) {
        final Vaccination vaccination = findById(id);
        vaccinationRepository.delete(vaccination);
    }

}
