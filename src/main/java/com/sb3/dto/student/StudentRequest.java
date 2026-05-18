package com.sb3.dto.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentRequest {
    private String anonymousCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String diagnosisInfo;
    private String parentPhone;
}
