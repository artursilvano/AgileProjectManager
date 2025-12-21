package com.arturcapelossi.agilepm.infrastructure.persistence.mapper;

import com.arturcapelossi.agilepm.domain.model.Sprint;
import com.arturcapelossi.agilepm.infrastructure.persistence.entity.SprintEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProjectMapper.class})
public interface SprintMapper {

    @Mapping(source = "projectEntity", target = "project")
    Sprint toModel(SprintEntity entity);

    @Mapping(source = "project", target = "projectEntity")
    SprintEntity toEntity(Sprint model);
}

