package dev.ilionx.workshop.support.util;

import dev.ilionx.workshop.Application;
import dev.ilionx.workshop.api.owner.repository.OwnerRepository;
import dev.ilionx.workshop.api.pet.repository.PetRepository;
import dev.ilionx.workshop.api.pet.repository.PetTypeRepository;
import dev.ilionx.workshop.api.vet.repository.SpecialtyRepository;
import dev.ilionx.workshop.api.vet.repository.VetRepository;
import dev.ilionx.workshop.api.visit.repository.VisitRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@SpringBootTest(
    classes = Application.class,
    webEnvironment = RANDOM_PORT
)
public class TestContextInitializer {

    // ========================= CONTEXT =========================
    @Autowired
    protected WebApplicationContext applicationContext;

    // ========================= REPOSITORIES =========================
    @Autowired
    protected OwnerRepository ownerRepository;

    @Autowired
    protected PetRepository petRepository;

    @Autowired
    protected PetTypeRepository petTypeRepository;

    @Autowired
    protected VetRepository vetRepository;

    @Autowired
    protected SpecialtyRepository specialtyRepository;

    @Autowired
    protected VisitRepository visitRepository;
}
