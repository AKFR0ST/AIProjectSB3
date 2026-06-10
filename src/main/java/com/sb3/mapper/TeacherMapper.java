package com.sb3.mapper;

import com.sb3.constant.TeacherStatus;
import com.sb3.dto.teacher.TeacherBriefDto;
import com.sb3.dto.teacher.TeacherRequest;
import com.sb3.dto.teacher.TeacherResponse;
import com.sb3.entity.teacher.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {

    public Teacher toEntity(TeacherRequest request) {
        if (request == null) return null;

        return Teacher.builder()
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .patronymic(request.getPatronymic())
                .email(request.getEmail())
                .phone(request.getPhone())
                .specialization(request.getSpecialization())
                .status(mapToEntityStatus(request.getStatus()))
                .build();
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
    }

    private TeacherStatus mapToEntityStatus(com.sb3.dto.teacher.TeacherStatusRequest status) {
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
}