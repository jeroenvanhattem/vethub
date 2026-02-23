package dev.ilionx.workshop.api.pet.repository;

import dev.ilionx.workshop.api.pet.model.PetType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link PetType} entities.
 */
@Repository
public interface PetTypeRepository extends JpaRepository<PetType, Integer> {
}
