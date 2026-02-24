package dev.ilionx.workshop.api.owner.model;

import dev.ilionx.workshop.api.pet.model.Pet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Entity representing a pet owner.
 */
@Entity
@Table(name = "owners")
@Getter
@Setter
@NoArgsConstructor
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // FIXED: Added @NotBlank validation
    @NotBlank(message = "First name cannot be blank")
    @Column(
        name = "first_name",
        nullable = false
    )
    private String firstName;

    // FIXED: Added @NotBlank validation
    @NotBlank(message = "Last name cannot be blank")
    @Column(
        name = "last_name",
        nullable = false
    )
    private String lastName;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "telephone")
    private String telephone;

    @Email(message = "Email must be valid")
    @Column(name = "email")
    private String email;

    @OneToMany(
        mappedBy = "owner",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER
    )
    private List<Pet> pets = new ArrayList<>();

}
