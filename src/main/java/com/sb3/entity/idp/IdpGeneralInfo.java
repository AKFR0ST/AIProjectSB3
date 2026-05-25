package com.sb3.entity.idp;

import com.sb3.entity.grid.Grid;
import com.sb3.entity.student.Student;
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
@Table(name = "idp_general_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpGeneralInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grid_id", unique = true)
    private Grid grid;

    @Column(nullable = false)
    @Builder.Default
    private Integer version = 1;

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

    @Column(name = "approved_at")
    private Instant approvedAt;

    @OneToMany(mappedBy = "idpGeneralInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IdpExercises> exercises = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}