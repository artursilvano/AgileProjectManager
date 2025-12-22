package com.arturcapelossi.agilepm.infrastructure.persistence.mapper;

import com.arturcapelossi.agilepm.domain.model.Task;
import com.arturcapelossi.agilepm.infrastructure.persistence.entity.TaskEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, UserStoryMapper.class, CommentMapper.class})
public interface TaskMapper {

    @Mapping(source = "userStory", target = "userStory")
    @Mapping(source = "commentEntities", target = "comments")
    Task toModel(TaskEntity entity, @Context CycleAvoidingMappingContext context);

    @Mapping(source = "userStory", target = "userStory")
    @Mapping(source = "commentEntities", target = "comments")
    List<Task> toModel(List<TaskEntity> entities, @Context CycleAvoidingMappingContext context);

    @Mapping(source = "userStory", target = "userStory")
    @Mapping(source = "comments", target = "commentEntities")
    TaskEntity toEntity(Task model, @Context CycleAvoidingMappingContext context);

    @Mapping(source = "userStory", target = "userStory")
    @Mapping(source = "comments", target = "commentEntities")
    List<TaskEntity> toEntity(List<Task> models, @Context CycleAvoidingMappingContext context);
}
