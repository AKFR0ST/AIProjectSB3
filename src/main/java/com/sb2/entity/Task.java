package com.sb2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Long gridId;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(columnDefinition = "text")
    private String result;

    private String error;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
