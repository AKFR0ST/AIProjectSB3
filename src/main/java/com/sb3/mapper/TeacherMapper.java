package com.sb3.mapper;

import com.sb3.constant.TeacherStatus;
import com.sb3.dto.student.StudentShortResponse;
import com.sb3.dto.teacher.TeacherBriefDto;
import com.sb3.dto.teacher.TeacherRequest;
import com.sb3.dto.teacher.TeacherResponse;
import com.sb3.dto.teacher.TeacherStatusRequest;
import com.sb3.entity.student.Student;
import com.sb3.entity.teacher.Teacher;
import com.sb3.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TeacherMapper {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public Teacher toEntity(TeacherRequest request) {
        if (request == null) return null;

        Teacher teacher = Teacher.builder()
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .patronymic(request.getPatronymic())
                .email(request.getEmail())
                .phone(request.getPhone())
                .specialization(request.getSpecialization())
                .status(mapToEntityStatus(request.getStatus()))
                .educations(request.getEducations() != null ? request.getEducations() : new ArrayList<>())
                .build();

        if (request.getStudentIds() != null && !request.getStudentIds().isEmpty()) {
            List<Student> students = studentRepository.findAllById(request.getStudentIds());
            teacher.setStudents(students);
        }

        return teacher;
    }

    public TeacherResponse toResponse(Teacher teacher) {
        if (teacher == null) return null;

        return TeacherResponse.builder()
                .id(teacher.getId())
                .lastName(teacher.getLastName())
                .firstName(teacher.getFirstName())
                .patronymic(teacher.getPatronymic())
                .fullName(getFullName(teacher))
                .email(teacher.getEmail())
                .phone(teacher.getPhone())
                .specialization(teacher.getSpecialization())
                .status(teacher.getStatus() != null ? teacher.getStatus().name() : null)
                .passwordUpdatedAt(teacher.getPasswordUpdatedAt())
                .createdAt(teacher.getCreatedAt())
                .updatedAt(teacher.getUpdatedAt())
                .students(mapStudentsToShortResponse(teacher.getStudents()))
                .educations(teacher.getEducations())  // Добавляем образования
                .build();
    }

    public TeacherBriefDto toBriefDto(Teacher teacher) {
        if (teacher == null) return null;

        return TeacherBriefDto.builder()
                .id(teacher.getId())
                .fullName(getFullName(teacher))
                .specialization(teacher.getSpecialization())
                .build();
    }

    public void updateEntity(Teacher teacher, TeacherRequest request) {
        if (request == null) return;

        if (request.getLastName() != null) teacher.setLastName(request.getLastName());
        if (request.getFirstName() != null) teacher.setFirstName(request.getFirstName());
        if (request.getPatronymic() != null) teacher.setPatronymic(request.getPatronymic());
        if (request.getEmail() != null) teacher.setEmail(request.getEmail());
        if (request.getPhone() != null) teacher.setPhone(request.getPhone());
        if (request.getSpecialization() != null) teacher.setSpecialization(request.getSpecialization());
        if (request.getStatus() != null) teacher.setStatus(mapToEntityStatus(request.getStatus()));
        if (request.getEducations() != null) teacher.setEducations(request.getEducations());

        if (request.getStudentIds() != null) {
            List<Student> students = studentRepository.findAllById(request.getStudentIds());
            teacher.setStudents(students);
        }
    }

    private TeacherStatus mapToEntityStatus(TeacherStatusRequest status) {
        if (status == null) return TeacherStatus.ACTIVE;
        return TeacherStatus.valueOf(status.name());
    }

    private String getFullName(Teacher teacher) {
        if (teacher == null) return null;

        StringBuilder fullName = new StringBuilder();
        if (teacher.getLastName() != null) fullName.append(teacher.getLastName());
        if (teacher.getFirstName() != null) {
            if (!fullName.isEmpty()) fullName.append(" ");
            fullName.append(teacher.getFirstName());
        }
        if (teacher.getPatronymic() != null && !teacher.getPatronymic().isBlank()) {
            if (!fullName.isEmpty()) fullName.append(" ");
            fullName.append(teacher.getPatronymic());
        }
        return fullName.toString();
    }

    private List<StudentShortResponse> mapStudentsToShortResponse(List<Student> students) {
        if (students == null || students.isEmpty()) {
            return Collections.emptyList();
        }

        return students.stream()
                .map(studentMapper::toShortResponse)
                .collect(Collectors.toList());
    }
}