package com.arturcapelossi.agilepm.application.usecase;

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
class RemoveUserStoryFromSprintUseCaseTest {

    @Mock
    private UserStoryRepository userStoryRepository;

    @InjectMocks
    private RemoveUserStoryFromSprintUseCase removeUserStoryFromSprintUseCase;

    private UserStory userStory;
    private Sprint sprint;
    private UUID userStoryId;
    private UUID sprintId;

    @BeforeEach
    void setUp() {
        userStoryId = UUID.randomUUID();
        sprintId = UUID.randomUUID();

        sprint = new Sprint();
        sprint.setId(sprintId);

        userStory = new UserStory();
        userStory.setId(userStoryId);
        userStory.setSprint(sprint);
    }

    @Test
    void execute_ShouldRemoveUserStoryFromSprint_WhenConditionsAreMet() {
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.of(userStory));
        when(userStoryRepository.save(any(UserStory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserStory updatedUserStory = removeUserStoryFromSprintUseCase.execute(sprintId, userStoryId);

        assertNull(updatedUserStory.getSprint());
        assertEquals(StoryStatus.BACKLOG, updatedUserStory.getStatus());
        verify(userStoryRepository).save(userStory);
    }

    @Test
    void execute_ShouldThrowException_WhenUserStoryNotFound() {
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> removeUserStoryFromSprintUseCase.execute(sprintId, userStoryId));
    }

    @Test
    void execute_ShouldThrowException_WhenUserStoryNotAssignedToSprint() {
        userStory.setSprint(null);
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.of(userStory));

        assertThrows(BusinessRuleException.class, () -> removeUserStoryFromSprintUseCase.execute(sprintId, userStoryId));
    }

    @Test
    void execute_ShouldThrowException_WhenUserStoryAssignedToDifferentSprint() {
        Sprint otherSprint = new Sprint();
        otherSprint.setId(UUID.randomUUID());
        userStory.setSprint(otherSprint);
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.of(userStory));

        assertThrows(BusinessRuleException.class, () -> removeUserStoryFromSprintUseCase.execute(sprintId, userStoryId));
    }
}

