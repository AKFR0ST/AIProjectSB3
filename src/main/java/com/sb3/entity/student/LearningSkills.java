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

    @Column(name = "attention_span_minutes")
    private Integer attentionSpanMinutes;

    @Column(name = "acquisition_speed")
    private String acquisitionSpeed;

    @Column(columnDefinition = "text")
    private String retention;

    @Column(name = "cooperates_with_adult")
    private Boolean cooperatesWithAdult;

    @Column(name = "follows_instructions")
    private Boolean followsInstructions;

    @Column(name = "requests_break_or_change")
    private Boolean requestsBreakOrChange;
}