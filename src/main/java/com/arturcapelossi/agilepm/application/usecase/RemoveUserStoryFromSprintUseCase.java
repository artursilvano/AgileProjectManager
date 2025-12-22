package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.BusinessRuleException;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.domain.model.enums.StoryStatus;
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

        if (userStory.getSprint() == null || !userStory.getSprint().getId().equals(sprintId)) {
            throw new BusinessRuleException("User Story is not assigned to this sprint.");
        }

        userStory.setSprint(null);
        userStory.setStatus(StoryStatus.BACKLOG);
        return userStoryRepository.save(userStory);
    }
}

