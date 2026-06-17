package com.sb3.service;

import com.sb3.constant.TeacherStatus;
import com.sb3.constant.UserRole;
import com.sb3.dto.student.StudentShortResponse;
import com.sb3.dto.teacher.TeacherRequest;
import com.sb3.dto.teacher.TeacherResponse;
import com.sb3.entity.teacher.Teacher;
import com.sb3.exception.DuplicateEmailException;
import com.sb3.exception.NotFoundException;
import com.sb3.mapper.StudentMapper;
import com.sb3.mapper.TeacherMapper;
import com.sb3.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<TeacherResponse> getAllTeachersOnly(Pageable pageable) {
        return teacherRepository.findAllByRole(UserRole.TEACHER, pageable)
                .map(teacherMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public TeacherResponse getTeacherById(Long id) {
        Teacher teacher = findTeacherById(id);
        return teacherMapper.toResponse(teacher);
    }

    @Transactional
    public TeacherResponse createTeacher(TeacherRequest request) {
        log.info("Creating new teacher: {} {}", request.getLastName(), request.getFirstName());

        // Проверяем уникальность email при создании
        if (teacherRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Teacher with email " + request.getEmail() + " already exists");
        }

        Teacher teacher = teacherMapper.toEntity(request);
        teacher.setPassword(passwordEncoder.encode(request.getPassword())); // временный пароль
        teacher = teacherRepository.save(teacher);

        log.info("Teacher created with id: {}", teacher.getId());
        return teacherMapper.toResponse(teacher);
    }

    @Transactional
    public TeacherResponse updateTeacher(Long id, TeacherRequest request) {
        log.info("Updating teacher with id: {}", id);

        Teacher teacher = findTeacherById(id);

        // Сохраняем старый email для сравнения
        String oldEmail = teacher.getEmail();
        String newEmail = request.getEmail();

        // Проверяем, изменился ли email
        if (!oldEmail.equals(newEmail)) {
            // Если email изменился, проверяем, не занят ли он другим учителем
            if (teacherRepository.existsByEmail(newEmail)) {
                throw new DuplicateEmailException("Teacher with email " + newEmail + " already exists");
            }
            log.info("Updating email from {} to {}", oldEmail, newEmail);
            teacher.setEmail(newEmail);
        }

        // Обновляем остальные поля (кроме email, если он не менялся)
        teacher.setLastName(request.getLastName());
        teacher.setFirstName(request.getFirstName());
        teacher.setPatronymic(request.getPatronymic());
        teacher.setPhone(request.getPhone());
        teacher.setSpecialization(request.getSpecialization());
        teacher.setStatus(TeacherStatus.valueOf(request.getStatus().name()));

        // Обновляем пароль только если он предоставлен
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            teacher.setPassword(passwordEncoder.encode(request.getPassword()));
            teacher.setPasswordUpdatedAt(LocalDateTime.now());
        }

        // Образование
        if (request.getEducations() != null) {
            // Очищаем старые образования
            teacher.getEducations().clear();
            // Добавляем новые
            teacher.getEducations().addAll(request.getEducations());
            log.info("Updated educations: {}", teacher.getEducations());
        }

        teacher = teacherRepository.save(teacher);

        return teacherMapper.toResponse(teacher);
    }

    @Transactional(readOnly = true)
    public List<StudentShortResponse> getTeacherStudents(Long teacherId) {
        log.info("Getting students for teacher with id: {}", teacherId);

        Teacher teacher = findTeacherById(teacherId);

        if (teacher.getStudents() == null || teacher.getStudents().isEmpty()) {
            return Collections.emptyList();
        }

        return teacher.getStudents().stream()
                .map(studentMapper::toShortResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TeacherResponse updateTeacherStatus(Long id, TeacherStatus status) {
        log.info("Updating teacher status: {} -> {}", id, status);

        Teacher teacher = findTeacherById(id);
        teacher.setStatus(status);
        teacher = teacherRepository.save(teacher);

        return teacherMapper.toResponse(teacher);
    }

    @Transactional
    public TeacherResponse updateTeacherRole(Long id, UserRole role) {
        log.info("Updating teacher role: {} -> {}", id, role);

        Teacher teacher = findTeacherById(id);
        teacher.setRole(role);
        teacher = teacherRepository.save(teacher);

        return teacherMapper.toResponse(teacher);
    }

    @Transactional
    public void deleteTeacher(Long id) {
        log.info("Deleting teacher with id: {}", id);

        if (!teacherRepository.existsById(id)) {
            throw new NotFoundException("Teacher not found with id: " + id);
        }

        teacherRepository.deleteById(id);
    }

    private Teacher findTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Teacher not found with id: " + id));
    }
}
