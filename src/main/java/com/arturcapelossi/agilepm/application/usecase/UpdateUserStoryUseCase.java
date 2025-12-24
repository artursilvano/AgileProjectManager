package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.api.dto.request.UpdateUserStoryRequest;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.UserStory;
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

        userStory.update(request.getTitle(), request.getDescription(), request.getPriority(), request.getStatus());

        return userStoryRepository.save(userStory);
    }
}
