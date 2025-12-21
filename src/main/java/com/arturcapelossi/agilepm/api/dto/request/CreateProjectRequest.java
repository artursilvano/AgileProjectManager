package com.arturcapelossi.agilepm.api.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateProjectRequest {
    private String name;
    private String description;
}

