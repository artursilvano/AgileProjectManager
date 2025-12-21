package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.BusinessRuleException;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Sprint;
import com.arturcapelossi.agilepm.domain.model.enums.SprintStatus;
import com.arturcapelossi.agilepm.domain.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StartSprintUseCase {

    private final SprintRepository sprintRepository;

    public Sprint execute(UUID sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint not found with id: " + sprintId));

        if (sprint.getStatus() != SprintStatus.PLANNED) {
            throw new BusinessRuleException("Only PLANNED sprints can be started.");
        }

        // Business Rule: Only one active sprint per project
        List<Sprint> activeSprints = sprintRepository.findByProjectIdAndStatus(sprint.getProject().getId(), SprintStatus.ACTIVE);
        if (!activeSprints.isEmpty()) {
            throw new BusinessRuleException("There is already an active sprint for this project.");
        }

        sprint.setStatus(SprintStatus.ACTIVE);
        return sprintRepository.save(sprint);
    }
}

