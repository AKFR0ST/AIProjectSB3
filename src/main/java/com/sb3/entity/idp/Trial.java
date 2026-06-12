package com.sb3.entity.idp;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "trials")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trial {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private IdpExercises exercise;

    @Column(nullable = false, length = 1)
    private String grade; // "С", "П", "О", "Н"

    @Column(name = "trial_date", nullable = false)
    private Instant trialDate;

    @Column(columnDefinition = "text")
    private String note;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}