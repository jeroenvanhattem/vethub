package dev.ilionx.workshop.api.vet.model.mapper;

import dev.ilionx.workshop.api.vet.model.Specialty;
import dev.ilionx.workshop.api.vet.model.response.SpecialtyResponse;
import io.github.jframe.util.mapper.config.SharedMapperConfig;

import java.util.List;

import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting Specialty entities to response DTOs.
 */
@Mapper(config = SharedMapperConfig.class)
public abstract class SpecialtyMapper {

    public abstract SpecialtyResponse toResponse(Specialty specialty);

    public abstract List<SpecialtyResponse> toResponseList(List<Specialty> specialties);
}
