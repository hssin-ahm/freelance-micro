package com.freelanceproject.task_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private Long clientId;  // The client who created the task
    private Long freelancerId;  // The freelancer assigned (nullable)

    @CreatedDate  // Auto-fills when the entity is created
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate  // Auto-updates when the entity is modified
    private LocalDateTime updatedAt;
}
