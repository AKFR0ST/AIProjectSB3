package com.sb3.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "problem_behavior")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemBehavior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String behavior;

    private String frequency;
    private String duration;

    @Column(name = "prevention_strategy")
    private String preventionStrategy;

    @Column(name = "intervention_strategy")
    private String interventionStrategy;

    @Column(name = "situations_never_occurs")
    private String situationsNeverOccurs;

    @Column(name = "high_risk_situations")
    private String highRiskSituations;

    @Column(name = "trigger_description")
    private String triggerDescription;
}