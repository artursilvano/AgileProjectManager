package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.domain.repository.UserStoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RemoveUserStoryFromSprintUseCase {

    private final UserStoryRepository userStoryRepository;

    @Transactional
    public UserStory execute(UUID sprintId, UUID userStoryId) {
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new ResourceNotFoundException("User Story not found with id: " + userStoryId));

        userStory.removeFromSprint(sprintId);
        return userStoryRepository.save(userStory);
    }
}
