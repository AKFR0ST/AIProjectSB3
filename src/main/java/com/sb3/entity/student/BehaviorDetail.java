package com.sb3.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "behavior_detail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "present")
    private Boolean present;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "duration_present")
    private String durationPresent;

    private String frequency;

    private String situations;

    @Column(name = "interruption_reaction")
    private String interruptionReaction;

    private String consequences;

    @Column(name = "how_to_stop")
    private String howToStop;
}