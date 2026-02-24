package dev.ilionx.workshop.api.appointment.repository;

import dev.ilionx.workshop.api.appointment.model.Appointment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Appointment entity.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    /**
     * Find all appointments for a specific pet.
     */
    List<Appointment> findByPetId(Integer petId);

    /**
     * Find all appointments for a specific vet.
     */
    List<Appointment> findByVetId(Integer vetId);

}
