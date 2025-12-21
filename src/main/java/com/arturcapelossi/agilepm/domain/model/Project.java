package com.arturcapelossi.agilepm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    private UUID id;
    private String name;
    private String description;
    private User owner;
    private Set<User> members = new HashSet<>();
    private List<UserStory> userStories = new ArrayList<>();
    private LocalDateTime createdAt;
}

