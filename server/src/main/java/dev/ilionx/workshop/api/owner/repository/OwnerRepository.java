package dev.ilionx.workshop.api.owner.repository;

import dev.ilionx.workshop.api.owner.model.Owner;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link Owner} entities.
 */
@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {

    /**
     * Finds all owners with the given last name.
     *
     * @param lastName the last name to search for
     * @return a list of owners matching the last name
     */
    List<Owner> findByLastName(String lastName);
}
