package com.arturcapelossi.agilepm.infrastructure.persistence.mapper;

import com.arturcapelossi.agilepm.domain.model.User;
import com.arturcapelossi.agilepm.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(User model);

    User toModel(UserEntity entity);
}
