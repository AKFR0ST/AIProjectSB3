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

    @Column(name = "how_to_avoid")
    private String howToAvoid;

    @Column(name = "how_to_stop")
    private String howToStop;

}