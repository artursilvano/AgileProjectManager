package com.arturcapelossi.agilepm.infrastructure.persistence.repository;

import com.arturcapelossi.agilepm.domain.model.enums.SprintStatus;
import com.arturcapelossi.agilepm.infrastructure.persistence.entity.SprintEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataSprintRepository extends JpaRepository<SprintEntity, UUID> {
    List<SprintEntity> findByProjectEntityIdAndStatus(UUID projectId, SprintStatus status);
}
