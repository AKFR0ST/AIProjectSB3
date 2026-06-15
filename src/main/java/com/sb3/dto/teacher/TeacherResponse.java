package com.sb3.dto.teacher;

import com.sb3.dto.student.StudentShortResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherResponse {
    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String fullName;
    private String email;
    private String phone;
    private String specialization;
    private String status;
    private LocalDateTime passwordUpdatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<StudentShortResponse> students;
    private List<String> educations;
}
