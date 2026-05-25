package com.sb3.dto.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentShortResponse {
    private Long id;
    private String studentCode;
    private LocalDate birthDate;
    private String diagnosis;
    private String schoolType;
    private String relativeName;
    private String relativePhone;
}
