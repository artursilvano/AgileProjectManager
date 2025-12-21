package com.arturcapelossi.agilepm.infrastructure.persistence.repository;

import com.arturcapelossi.agilepm.domain.model.User;
import com.arturcapelossi.agilepm.domain.repository.UserRepository;
import com.arturcapelossi.agilepm.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class JpaUserRepository implements UserRepository {

    private final SpringDataUserRepository jpaRepo;
    private final UserMapper mapper;

    public JpaUserRepository(SpringDataUserRepository jpaRepo, UserMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        var entity = mapper.toEntity(user);
        var savedEntity = jpaRepo.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepo.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepo.findByEmail(email).map(mapper::toModel);
    }

    @Override
    public void delete(UUID id) {
        jpaRepo.deleteById(id);
    }
}
