package dev.ilionx.workshop.api.vet.repository;

import dev.ilionx.workshop.api.vet.model.Vet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link Vet} entities.
 */
@Repository
public interface VetRepository extends JpaRepository<Vet, Integer> {
}
