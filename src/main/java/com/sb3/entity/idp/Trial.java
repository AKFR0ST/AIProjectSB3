package com.sb3.entity.idp;

import com.sb3.constant.TrialMode;
import com.sb3.constant.TrialResult;
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

    @Column(name = "target", length = 500)
    private String target;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrialMode mode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrialResult result;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}