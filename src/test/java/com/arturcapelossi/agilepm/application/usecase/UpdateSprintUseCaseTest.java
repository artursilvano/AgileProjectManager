package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.api.dto.request.UpdateSprintRequest;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Sprint;
import com.arturcapelossi.agilepm.domain.repository.SprintRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateSprintUseCaseTest {

    @Mock
    private SprintRepository sprintRepository;

    @InjectMocks
    private UpdateSprintUseCase updateSprintUseCase;

    private Sprint sprint;
    private UUID sprintId;
    private UpdateSprintRequest request;

    @BeforeEach
    void setUp() {
        sprintId = UUID.randomUUID();
        sprint = new Sprint();
        sprint.setId(sprintId);
        sprint.setName("Old Name");

        request = new UpdateSprintRequest();
        request.setName("New Name");
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(14));
    }

    @Test
    void execute_ShouldUpdateSprint_WhenSprintExists() {
        when(sprintRepository.findById(sprintId)).thenReturn(Optional.of(sprint));
        when(sprintRepository.save(any(Sprint.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Sprint updatedSprint = updateSprintUseCase.execute(sprintId, request);

        assertEquals("New Name", updatedSprint.getName());
        assertEquals(request.getStartDate(), updatedSprint.getStartDate());
        assertEquals(request.getEndDate(), updatedSprint.getEndDate());
        verify(sprintRepository).save(sprint);
    }

    @Test
    void execute_ShouldThrowException_WhenSprintNotFound() {
        when(sprintRepository.findById(sprintId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> updateSprintUseCase.execute(sprintId, request));
    }
}

