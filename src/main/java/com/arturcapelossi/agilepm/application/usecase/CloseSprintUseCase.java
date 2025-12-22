package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.BusinessRuleException;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Sprint;
import com.arturcapelossi.agilepm.domain.model.enums.SprintStatus;
import com.arturcapelossi.agilepm.domain.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CloseSprintUseCase {

    private final SprintRepository sprintRepository;

    @Transactional
    public Sprint execute(UUID sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint not found with id: " + sprintId));

        if (sprint.getStatus() != SprintStatus.ACTIVE) {
            throw new BusinessRuleException("Only ACTIVE sprints can be closed.");
        }

        sprint.setStatus(SprintStatus.CLOSED);
        return sprintRepository.save(sprint);
    }
}

