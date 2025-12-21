package com.arturcapelossi.agilepm.infrastructure.persistence.entity;

import com.arturcapelossi.agilepm.domain.model.enums.SprintStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "sprints")
@Getter
@Setter
@NoArgsConstructor
public class SprintEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SprintStatus status = SprintStatus.PLANNED;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity projectEntity;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public SprintEntity(String name, LocalDate startDate, LocalDate endDate, ProjectEntity projectEntity) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectEntity = projectEntity;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SprintEntity)) return false;
        SprintEntity sprintEntity = (SprintEntity) o;
        if (id != null && sprintEntity.id != null) return Objects.equals(id, sprintEntity.id);
        return Objects.equals(name, sprintEntity.name);
    }

    @Override
    public int hashCode() {
        if (id != null) return Objects.hash(id);
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Sprint{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
