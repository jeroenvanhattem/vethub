package dev.ilionx.workshop.api.vaccination.model.mapper;

import dev.ilionx.workshop.api.vaccination.model.Vaccination;
import dev.ilionx.workshop.api.vaccination.model.response.VaccinationResponse;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for Vaccination entity to response DTO.
 */
@Mapper(componentModel = "spring")
public interface VaccinationMapper {

    @Mapping(
        target = "petId",
        source = "pet.id"
    )
    @Mapping(
        target = "petName",
        source = "pet.name"
    )
    VaccinationResponse toResponse(Vaccination vaccination);

    List<VaccinationResponse> toResponseList(List<Vaccination> vaccinations);

}
