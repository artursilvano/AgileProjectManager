package com.arturcapelossi.agilepm.infrastructure.persistence.mapper;

import com.arturcapelossi.agilepm.domain.model.Task;
import com.arturcapelossi.agilepm.infrastructure.persistence.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, UserStoryMapper.class})
public interface TaskMapper {

    @Mapping(source = "userStory", target = "userStory")
    @Mapping(target = "comments", ignore = true)
    Task toModel(TaskEntity entity);

    @Mapping(source = "userStory", target = "userStory")
    @Mapping(target = "comments", ignore = true)
    List<Task> toModel(List<TaskEntity> entities);

    @Mapping(source = "userStory", target = "userStory")
    @Mapping(target = "commentEntities", ignore = true)
    TaskEntity toEntity(Task model);
}

