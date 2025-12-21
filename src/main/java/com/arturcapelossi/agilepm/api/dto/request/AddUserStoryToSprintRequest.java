package com.arturcapelossi.agilepm.api.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class AddUserStoryToSprintRequest {
    private UUID userStoryId;
}

