package com.arturcapelossi.agilepm.domain.repository;

import com.arturcapelossi.agilepm.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    void delete(UUID id);
}

