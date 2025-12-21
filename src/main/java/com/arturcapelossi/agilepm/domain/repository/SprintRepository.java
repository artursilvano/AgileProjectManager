package com.arturcapelossi.agilepm.domain.repository;

import com.arturcapelossi.agilepm.domain.model.Sprint;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SprintRepository {
    Sprint save(Sprint sprint);
    Optional<Sprint> findById(UUID id);
    List<Sprint> findByProjectId(UUID projectId);
    void delete(UUID id);
}

