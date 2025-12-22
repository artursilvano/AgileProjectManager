package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.api.dto.request.UpdateUserStoryRequest;
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
public class UpdateUserStoryUseCase {

    private final UserStoryRepository userStoryRepository;

    @Transactional
    public UserStory execute(UUID id, UpdateUserStoryRequest request) {
        UserStory userStory = userStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Story not found with id: " + id));

        if (request.getTitle() != null) {
            userStory.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            userStory.setDescription(request.getDescription());
        }
        if (request.getPriority() != null) {
            userStory.setPriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            if (userStory.getSprint() == null && request.getStatus() != StoryStatus.BACKLOG) {
                throw new BusinessRuleException("Cannot change status of a user story in backlog unless it is assigned to a sprint.");
            }
            if (request.getStatus() == StoryStatus.BACKLOG) {
                userStory.setSprint(null);
            }
            userStory.setStatus(request.getStatus());
        }

        return userStoryRepository.save(userStory);
    }
}
