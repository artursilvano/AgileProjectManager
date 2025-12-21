package com.arturcapelossi.agilepm.domain.repository;

import com.arturcapelossi.agilepm.domain.model.UserStory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStoryRepository {
    UserStory save(UserStory userStory);
    Optional<UserStory> findById(UUID id);
    List<UserStory> findByProjectId(UUID projectId);
    List<UserStory> findBySprintId(UUID sprintId);
    void delete(UUID id);
}

