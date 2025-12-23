package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.api.dto.request.UpdateUserStoryRequest;
import com.arturcapelossi.agilepm.domain.exception.BusinessRuleException;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Sprint;
import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.domain.model.enums.StoryStatus;
import com.arturcapelossi.agilepm.domain.repository.UserStoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserStoryUseCaseTest {

    @Mock
    private UserStoryRepository userStoryRepository;

    @InjectMocks
    private UpdateUserStoryUseCase updateUserStoryUseCase;

    private UserStory userStory;
    private UUID userStoryId;
    private UpdateUserStoryRequest request;

    @BeforeEach
    void setUp() {
        userStoryId = UUID.randomUUID();
        userStory = new UserStory();
        userStory.setId(userStoryId);
        userStory.setTitle("Old Title");
        userStory.setStatus(StoryStatus.TODO);
        userStory.setSprint(new Sprint());

        request = new UpdateUserStoryRequest();
        request.setTitle("New Title");
        request.setStatus(StoryStatus.DOING);
    }

    @Test
    void execute_ShouldUpdateUserStory_WhenConditionsAreMet() {
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.of(userStory));
        when(userStoryRepository.save(any(UserStory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserStory updatedUserStory = updateUserStoryUseCase.execute(userStoryId, request);

        assertEquals("New Title", updatedUserStory.getTitle());
        assertEquals(StoryStatus.DOING, updatedUserStory.getStatus());
        verify(userStoryRepository).save(userStory);
    }

    @Test
    void execute_ShouldThrowException_WhenUserStoryNotFound() {
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> updateUserStoryUseCase.execute(userStoryId, request));
    }

    @Test
    void execute_ShouldThrowException_WhenUpdatingStatusAndUserStoryInBacklog() {
        userStory.setSprint(null);
        userStory.setStatus(StoryStatus.BACKLOG);
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.of(userStory));

        assertThrows(BusinessRuleException.class, () -> updateUserStoryUseCase.execute(userStoryId, request));
    }
}

