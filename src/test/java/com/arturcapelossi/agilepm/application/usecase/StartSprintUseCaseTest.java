package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.BusinessRuleException;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Project;
import com.arturcapelossi.agilepm.domain.model.Sprint;
import com.arturcapelossi.agilepm.domain.model.enums.SprintStatus;
import com.arturcapelossi.agilepm.domain.repository.SprintRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartSprintUseCaseTest {

    @Mock
    private SprintRepository sprintRepository;

    @InjectMocks
    private StartSprintUseCase startSprintUseCase;

    private Sprint sprint;
    private Project project;
    private UUID sprintId;
    private UUID projectId;

    @BeforeEach
    void setUp() {
        sprintId = UUID.randomUUID();
        projectId = UUID.randomUUID();

        project = new Project();
        project.setId(projectId);

        sprint = new Sprint();
        sprint.setId(sprintId);
        sprint.setProject(project);
        sprint.setStatus(SprintStatus.PLANNED);
    }

    @Test
    void execute_ShouldStartSprint_WhenConditionsAreMet() {
        when(sprintRepository.findById(sprintId)).thenReturn(Optional.of(sprint));
        when(sprintRepository.findByProjectIdAndStatus(projectId, SprintStatus.ACTIVE)).thenReturn(Collections.emptyList());
        when(sprintRepository.save(any(Sprint.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Sprint startedSprint = startSprintUseCase.execute(sprintId);

        assertEquals(SprintStatus.ACTIVE, startedSprint.getStatus());
        verify(sprintRepository).save(sprint);
    }

    @Test
    void execute_ShouldThrowException_WhenSprintNotFound() {
        when(sprintRepository.findById(sprintId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> startSprintUseCase.execute(sprintId));
    }

    @Test
    void execute_ShouldThrowException_WhenSprintIsNotPlanned() {
        sprint.setStatus(SprintStatus.CLOSED);
        when(sprintRepository.findById(sprintId)).thenReturn(Optional.of(sprint));

        assertThrows(BusinessRuleException.class, () -> startSprintUseCase.execute(sprintId));
    }

    @Test
    void execute_ShouldThrowException_WhenActiveSprintExists() {
        when(sprintRepository.findById(sprintId)).thenReturn(Optional.of(sprint));
        when(sprintRepository.findByProjectIdAndStatus(projectId, SprintStatus.ACTIVE)).thenReturn(List.of(new Sprint()));

        assertThrows(BusinessRuleException.class, () -> startSprintUseCase.execute(sprintId));
    }
}

