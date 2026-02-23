package dev.ilionx.workshop.api.pet.model.mapper;

import dev.ilionx.workshop.api.pet.model.PetType;
import dev.ilionx.workshop.api.pet.model.response.PetTypeResponse;
import io.github.jframe.util.mapper.config.SharedMapperConfig;

import java.util.List;

import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting PetType entities to response DTOs.
 */
@Mapper(config = SharedMapperConfig.class)
public abstract class PetTypeMapper {

    public abstract PetTypeResponse toResponse(PetType petType);

    public abstract List<PetTypeResponse> toResponseList(List<PetType> petTypes);
}
