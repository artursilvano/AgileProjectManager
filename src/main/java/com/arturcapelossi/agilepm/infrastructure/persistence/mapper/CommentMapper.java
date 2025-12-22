package com.arturcapelossi.agilepm.infrastructure.persistence.mapper;

import com.arturcapelossi.agilepm.domain.model.Comment;
import com.arturcapelossi.agilepm.infrastructure.persistence.entity.CommentEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class CommentMapper {

    @Autowired
    @Lazy
    protected TaskMapper taskMapper;

    @Mapping(source = "task", target = "task")
    public abstract Comment toModel(CommentEntity entity, @Context CycleAvoidingMappingContext context);

    @Mapping(source = "task", target = "task")
    public abstract CommentEntity toEntity(Comment model, @Context CycleAvoidingMappingContext context);
}

