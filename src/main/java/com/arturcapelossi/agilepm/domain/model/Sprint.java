package com.arturcapelossi.agilepm.domain.model;

import com.arturcapelossi.agilepm.domain.exception.BusinessRuleException;
import com.arturcapelossi.agilepm.domain.model.enums.SprintStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sprint {
    private UUID id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private SprintStatus status;
    private Project project;
    private LocalDateTime createdAt;

    public void start() {
        if (this.status != SprintStatus.PLANNED) {
            throw new BusinessRuleException("Only PLANNED sprints can be started. Current status: " + this.status);
        }
        this.status = SprintStatus.ACTIVE;
    }

    public void close() {
        if (this.status != SprintStatus.ACTIVE) {
            throw new BusinessRuleException("Only ACTIVE sprints can be closed.");
        }
        this.status = SprintStatus.CLOSED;
    }

    public static Sprint create(String name, LocalDate startDate, LocalDate endDate, Project project) {
        Sprint sprint = new Sprint();
        sprint.setId(UUID.randomUUID());
        sprint.setName(name);
        sprint.setStartDate(startDate);
        sprint.setEndDate(endDate);
        sprint.setStatus(SprintStatus.PLANNED);
        sprint.setProject(project);
        sprint.setCreatedAt(LocalDateTime.now());
        return sprint;
    }

    public void update(String name, LocalDate startDate, LocalDate endDate) {
        if (name != null) {
            this.name = name;
        }
        if (startDate != null) {
            this.startDate = startDate;
        }
        if (endDate != null) {
            this.endDate = endDate;
        }
    }
}