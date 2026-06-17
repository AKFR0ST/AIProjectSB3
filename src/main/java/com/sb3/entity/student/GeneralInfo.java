package com.sb3.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "general_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "diagnosis_and_health_status")
    private String diagnosisAndHealthStatus;

    @Column(name = "development_history_from_pregnancy_birth", columnDefinition = "text")
    private String developmentHistoryFromPregnancyBirth;
}