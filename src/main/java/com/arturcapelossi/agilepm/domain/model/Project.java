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

    public static Project create(String name, String description, User owner) {
        Project project = new Project();
        project.setId(UUID.randomUUID());
        project.setName(name);
        project.setDescription(description);
        project.setOwner(owner);
        project.getMembers().add(owner);
        project.setCreatedAt(LocalDateTime.now());
        return project;
    }

    public void addMember(User user) {
        this.members.add(user);
    }

    public void update(String name, String description) {
        if (name != null) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
    }
}
