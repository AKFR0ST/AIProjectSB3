package com.sb3.entity.idp;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "idp_exercises")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpExercises {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idp_general_info_id", nullable = false)
    private IdpGeneralInfo idpGeneralInfo;

    @Column(name = "skill_codes", columnDefinition = "text[]", nullable = false)
    private List<String> skillCodes = new ArrayList<>();

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "draft";

    @Column(name = "content", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String content;

    @Column(name = "original_content", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String originalContent;

    @Column(name = "edits", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String edits;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}