package com.arturcapelossi.agilepm.infrastructure.persistence.mapper;

import com.arturcapelossi.agilepm.domain.model.Task;
import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.infrastructure.persistence.entity.TaskEntity;
import com.arturcapelossi.agilepm.infrastructure.persistence.entity.UserStoryEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SprintMapper.class, ProjectMapper.class})
public abstract class UserStoryMapper {

    protected TaskMapper taskMapper;

    @Autowired
    public void setTaskMapper(@Lazy TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Mapping(source = "sprintEntity", target = "sprint")
    @Mapping(source = "projectEntity", target = "project")
    @Mapping(source = "taskEntities", target = "tasks")
    public abstract UserStory toModel(UserStoryEntity entity, @Context CycleAvoidingMappingContext context);

    @Mapping(source = "sprint", target = "sprintEntity")
    @Mapping(source = "project", target = "projectEntity")
    @Mapping(source = "tasks", target = "taskEntities")
    public abstract UserStoryEntity toEntity(UserStory model, @Context CycleAvoidingMappingContext context);

    protected List<TaskEntity> mapTasks(List<Task> tasks, @Context CycleAvoidingMappingContext context) {
        return taskMapper.toEntity(tasks, context);
    }

    protected List<Task> mapTaskEntities(java.util.List<TaskEntity> taskEntities, @Context CycleAvoidingMappingContext context) {
        return taskMapper.toModel(taskEntities, context);
    }
}
