package com.sb3.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "self_stimulatory_behavior")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelfStimulatoryBehavior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String behavior;

    private String situations;
    private String frequency;
}