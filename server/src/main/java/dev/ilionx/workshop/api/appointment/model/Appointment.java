package dev.ilionx.workshop.api.appointment.model;

import dev.ilionx.workshop.api.pet.model.Pet;
import dev.ilionx.workshop.api.vet.model.Vet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Entity representing a scheduled appointment.
 */
@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Scheduled date/time cannot be null")
    @Future(message = "Scheduled date/time must be in the future")
    @Column(
        name = "scheduled_date_time",
        nullable = false
    )
    private LocalDateTime scheduledDateTime;

    @NotBlank(message = "Reason cannot be blank")
    @Column(
        name = "reason",
        nullable = false
    )
    private String reason;

    @NotNull(message = "Status cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(
        name = "status",
        nullable = false
    )
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @ManyToOne
    @JoinColumn(
        name = "pet_id",
        nullable = false
    )
    private Pet pet;

    @ManyToOne
    @JoinColumn(
        name = "vet_id",
        nullable = false
    )
    private Vet vet;

}
