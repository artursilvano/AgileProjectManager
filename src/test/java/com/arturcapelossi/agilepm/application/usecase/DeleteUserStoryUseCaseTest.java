package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.UserStory;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserStoryUseCaseTest {

    @Mock
    private UserStoryRepository userStoryRepository;

    @InjectMocks
    private DeleteUserStoryUseCase deleteUserStoryUseCase;

    private UserStory userStory;
    private UUID userStoryId;

    @BeforeEach
    void setUp() {
        userStoryId = UUID.randomUUID();
        userStory = new UserStory();
        userStory.setId(userStoryId);
    }

    @Test
    void execute_ShouldDeleteUserStory_WhenUserStoryExists() {
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.of(userStory));

        deleteUserStoryUseCase.execute(userStoryId);

        verify(userStoryRepository).delete(userStoryId);
    }

    @Test
    void execute_ShouldThrowException_WhenUserStoryNotFound() {
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> deleteUserStoryUseCase.execute(userStoryId));
        verify(userStoryRepository, never()).delete(any());
    }
}

