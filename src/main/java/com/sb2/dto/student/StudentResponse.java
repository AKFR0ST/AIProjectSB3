package com.sb2.dto.student;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentResponse {
    private Long id;
    private String anonymousCode;
//    private LocalDate birthDate;
    private String diagnosisInfo;
}