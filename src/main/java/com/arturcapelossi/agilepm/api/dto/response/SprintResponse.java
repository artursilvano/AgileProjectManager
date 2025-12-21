package com.arturcapelossi.agilepm.api.dto.response;

import com.arturcapelossi.agilepm.domain.model.enums.SprintStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SprintResponse {
    private UUID id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private SprintStatus status;
    private UUID projectId;
    private LocalDateTime createdAt;
}

