package dev.ilionx.workshop.api.vaccination.model;

import dev.ilionx.workshop.api.pet.model.Pet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

/**
 * Entity representing a vaccination record for a pet.
 */
@Entity
@Table(name = "vaccinations")
@Getter
@Setter
@NoArgsConstructor
public class Vaccination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Vaccine name cannot be blank")
    @Column(
        name = "vaccine_name",
        nullable = false
    )
    private String vaccineName;

    @NotNull(message = "Vaccination date cannot be null")
    @Past(message = "Vaccination date must be in the past")
    @Column(
        name = "vaccination_date",
        nullable = false
    )
    private LocalDate vaccinationDate;

    @Column(name = "next_due_date")
    private LocalDate nextDueDate;

    @ManyToOne
    @JoinColumn(
        name = "pet_id",
        nullable = false
    )
    private Pet pet;

}
