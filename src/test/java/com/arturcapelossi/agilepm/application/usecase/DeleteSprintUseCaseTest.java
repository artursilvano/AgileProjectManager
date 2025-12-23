package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Sprint;
import com.arturcapelossi.agilepm.domain.repository.SprintRepository;
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
class DeleteSprintUseCaseTest {

    @Mock
    private SprintRepository sprintRepository;

    @InjectMocks
    private DeleteSprintUseCase deleteSprintUseCase;

    private Sprint sprint;
    private UUID sprintId;

    @BeforeEach
    void setUp() {
        sprintId = UUID.randomUUID();
        sprint = new Sprint();
        sprint.setId(sprintId);
    }

    @Test
    void execute_ShouldDeleteSprint_WhenSprintExists() {
        when(sprintRepository.findById(sprintId)).thenReturn(Optional.of(sprint));

        deleteSprintUseCase.execute(sprintId);

        verify(sprintRepository).delete(sprintId);
    }

    @Test
    void execute_ShouldThrowException_WhenSprintNotFound() {
        when(sprintRepository.findById(sprintId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> deleteSprintUseCase.execute(sprintId));
        verify(sprintRepository, never()).delete(any());
    }
}

