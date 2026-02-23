package dev.ilionx.workshop.api.vet.repository;

import dev.ilionx.workshop.api.vet.model.Specialty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link Specialty} entities.
 */
@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {
}
