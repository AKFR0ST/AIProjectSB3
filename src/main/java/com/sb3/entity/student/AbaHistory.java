package com.sb3.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aba_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbaHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "has_prior_aba")
    private Boolean hasPriorAba;

    @Column(name = "aba_duration_intensity")
    private String abaDurationIntensity;

    @Column(name = "other_developmental_activities")
    private String otherDevelopmentalActivities;

    @Column(name = "attends_kindergarten")
    private Boolean attendsKindergarten;

    @Column(name = "kindergarten_details")
    private String kindergartenDetails;
}