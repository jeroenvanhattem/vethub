package dev.ilionx.workshop.api.vaccination.repository;

import dev.ilionx.workshop.api.vaccination.model.Vaccination;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Vaccination entity.
 */
@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Integer> {

    /**
     * Find all vaccinations for a specific pet.
     *
     * @param petId the pet ID
     * @return list of vaccinations
     */
    List<Vaccination> findByPetId(Integer petId);

}
