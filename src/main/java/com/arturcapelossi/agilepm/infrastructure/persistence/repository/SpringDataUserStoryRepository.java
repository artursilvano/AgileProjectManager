package com.arturcapelossi.agilepm.infrastructure.persistence.repository;

import com.arturcapelossi.agilepm.infrastructure.persistence.entity.UserStoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataUserStoryRepository extends JpaRepository<UserStoryEntity, UUID> {
}

