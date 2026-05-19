package com.sb3.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medical_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "psychiatric_diagnosis")
    private String psychiatricDiagnosis;

    @Column(name = "developmental_history", columnDefinition = "text")
    private String developmentalHistory;

    @Column(name = "health_conditions", columnDefinition = "text")
    private String healthConditions;
}