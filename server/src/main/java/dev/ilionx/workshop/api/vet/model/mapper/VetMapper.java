package dev.ilionx.workshop.api.vet.model.mapper;

import dev.ilionx.workshop.api.vet.model.Specialty;
import dev.ilionx.workshop.api.vet.model.Vet;
import dev.ilionx.workshop.api.vet.model.response.SpecialtyResponse;
import dev.ilionx.workshop.api.vet.model.response.VetResponse;
import io.github.jframe.util.mapper.config.SharedMapperConfig;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting Vet entities to response DTOs.
 */
@Mapper(config = SharedMapperConfig.class)
public abstract class VetMapper {

    public abstract VetResponse toResponse(Vet vet);

    public abstract List<VetResponse> toResponseList(List<Vet> vets);

    public abstract SpecialtyResponse toSpecialtyResponse(Specialty specialty);

    public List<SpecialtyResponse> toSpecialtyResponseList(final Set<Specialty> specialties) {
        return specialties.stream()
            .sorted((a, b) -> Integer.compare(a.getId(), b.getId()))
            .map(this::toSpecialtyResponse)
            .collect(Collectors.toList());
    }
}
