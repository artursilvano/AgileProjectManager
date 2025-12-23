package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.BusinessRuleException;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Sprint;
import com.arturcapelossi.agilepm.domain.model.enums.SprintStatus;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CloseSprintUseCaseTest {

    @Mock
    private SprintRepository sprintRepository;

    @InjectMocks
    private CloseSprintUseCase closeSprintUseCase;

    private Sprint sprint;
    private UUID sprintId;

    @BeforeEach
    void setUp() {
        sprintId = UUID.randomUUID();
        sprint = new Sprint();
        sprint.setId(sprintId);
        sprint.setStatus(SprintStatus.ACTIVE);
    }

    @Test
    void execute_ShouldCloseSprint_WhenSprintIsActive() {
        when(sprintRepository.findById(sprintId)).thenReturn(Optional.of(sprint));
        when(sprintRepository.save(any(Sprint.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Sprint closedSprint = closeSprintUseCase.execute(sprintId);

        assertEquals(SprintStatus.CLOSED, closedSprint.getStatus());
        verify(sprintRepository).save(sprint);
    }

    @Test
    void execute_ShouldThrowException_WhenSprintNotFound() {
        when(sprintRepository.findById(sprintId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> closeSprintUseCase.execute(sprintId));
    }

    @Test
    void execute_ShouldThrowException_WhenSprintIsNotActive() {
        sprint.setStatus(SprintStatus.PLANNED);
        when(sprintRepository.findById(sprintId)).thenReturn(Optional.of(sprint));

        assertThrows(BusinessRuleException.class, () -> closeSprintUseCase.execute(sprintId));
    }
}

