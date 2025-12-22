package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.BusinessRuleException;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Sprint;
import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.domain.model.enums.StoryStatus;
import com.arturcapelossi.agilepm.domain.repository.SprintRepository;
import com.arturcapelossi.agilepm.domain.repository.UserStoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddUserStoryToSprintUseCase {

    private final UserStoryRepository userStoryRepository;
    private final SprintRepository sprintRepository;

    public UserStory execute(UUID userStoryId, UUID sprintId) {
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new ResourceNotFoundException("UserStory not found with id: " + userStoryId));

        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint not found with id: " + sprintId));

        if (!userStory.getProject().getId().equals(sprint.getProject().getId())) {
            throw new BusinessRuleException("UserStory and Sprint must belong to the same project.");
        }

        userStory.setSprint(sprint);
        userStory.setStatus(StoryStatus.TODO);
        return userStoryRepository.save(userStory);
    }
}
