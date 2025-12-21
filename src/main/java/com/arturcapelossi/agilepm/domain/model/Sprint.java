package com.arturcapelossi.agilepm.domain.model;

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
}