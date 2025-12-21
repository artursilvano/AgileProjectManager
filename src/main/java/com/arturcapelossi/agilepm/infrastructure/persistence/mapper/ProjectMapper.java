package com.arturcapelossi.agilepm.infrastructure.persistence.mapper;

import com.arturcapelossi.agilepm.domain.model.Project;
import com.arturcapelossi.agilepm.infrastructure.persistence.entity.ProjectEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ProjectMapper {

    Project toModel(ProjectEntity entity);

    ProjectEntity toEntity(Project model);
}

