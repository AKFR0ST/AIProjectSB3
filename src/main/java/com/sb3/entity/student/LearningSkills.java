package com.sb3.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "learning_skills")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningSkills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "can_work_at_table")
    private Boolean canWorkAtTable;

    @Column(name = "task_attention_span_minutes")
    private Integer taskAttentionSpanMinutes;

    @Column(name = "acquisition_speed")
    private String acquisitionSpeed;

    @Column(name = "skills_retained", columnDefinition = "text")
    private String skillsRetained;

    @Column(name = "works_jointly_with_adult")
    private Boolean worksJointlyWithAdult;

    @Column(name = "follows_adult_instructions")
    private Boolean followsAdultInstructions;

    @Column(name = "requests_break_help_or_change")
    private Boolean requestsBreakHelpOrChange;
}