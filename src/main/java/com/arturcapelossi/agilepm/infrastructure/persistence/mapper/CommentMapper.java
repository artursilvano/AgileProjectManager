package com.arturcapelossi.agilepm.infrastructure.persistence.mapper;

import com.arturcapelossi.agilepm.domain.model.Comment;
import com.arturcapelossi.agilepm.infrastructure.persistence.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TaskMapper.class})
public interface CommentMapper {

    @Mapping(source = "task", target = "task")
    Comment toModel(CommentEntity entity);

    @Mapping(source = "task", target = "task")
    CommentEntity toEntity(Comment model);
}

