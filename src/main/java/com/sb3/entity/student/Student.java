package com.sb3.entity.student;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "student")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(description = "Идентификатор ученика", example = "СП19")
    private String anonymousCode;
    @Schema(description = "Дата рождения", example = "29.02.2020")
    private LocalDate birthDate;
    @Column(columnDefinition = "text")
    @Schema(description = "Особенности", example = "Моторика")
    private String diagnosisInfo;
    @Schema(description = "Телефон родителей", example = "+7(999)-999-99-99")
    private String parentPhone;
}
