package com.arturcapelossi.agilepm.infrastructure.persistence.mapper;

import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.infrastructure.persistence.entity.UserStoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SprintMapper.class, ProjectMapper.class})
public interface UserStoryMapper {

    @Mapping(source = "sprintEntity", target = "sprint")
    @Mapping(source = "projectEntity", target = "project")
    @Mapping(target = "tasks", ignore = true)
    UserStory toModel(UserStoryEntity entity);

    @Mapping(source = "sprint", target = "sprintEntity")
    @Mapping(source = "project", target = "projectEntity")
    @Mapping(target = "taskEntities", ignore = true)
    UserStoryEntity toEntity(UserStory model);
}

