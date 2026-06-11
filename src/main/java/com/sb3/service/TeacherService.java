package com.sb3.service;

import com.sb3.constant.TeacherStatus;
import com.sb3.constant.UserRole;
import com.sb3.dto.teacher.TeacherRequest;
import com.sb3.dto.teacher.TeacherResponse;
import com.sb3.entity.teacher.Teacher;
import com.sb3.exception.NotFoundException;
import com.sb3.mapper.TeacherMapper;
import com.sb3.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    @Transactional
    public TeacherResponse createTeacher(TeacherRequest request) {
        log.info("Creating new teacher: {} {}", request.getLastName(), request.getFirstName());

        Teacher teacher = teacherMapper.toEntity(request);
        teacher = teacherRepository.save(teacher);

        log.info("Teacher created with id: {}", teacher.getId());
        return teacherMapper.toResponse(teacher);
    }

    @Transactional(readOnly = true)
    public List<TeacherResponse> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(teacherMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TeacherResponse getTeacherById(Long id) {
        Teacher teacher = findTeacherById(id);
        return teacherMapper.toResponse(teacher);
    }

    @Transactional
    public TeacherResponse updateTeacher(Long id, TeacherRequest request) {
        log.info("Updating teacher with id: {}", id);

        Teacher teacher = findTeacherById(id);
        teacherMapper.updateEntity(teacher, request);
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

    @Transactional
    public TeacherResponse updateTeacherStatus(Long id, TeacherStatus status) {
        log.info("Updating teacher status: {} -> {}", id, status);

        Teacher teacher = findTeacherById(id);
        teacher.setStatus(status);
        teacher = teacherRepository.save(teacher);

        return teacherMapper.toResponse(teacher);
    }

    @Transactional
    public TeacherResponse updatePasswordUpdatedAt(Long id) {
        Teacher teacher = findTeacherById(id);
        teacher.setPasswordUpdatedAt(LocalDateTime.now());
        teacher = teacherRepository.save(teacher);

        return teacherMapper.toResponse(teacher);
    }

    private Teacher findTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Teacher not found with id: " + id));
    }

    @Transactional
    public TeacherResponse updateTeacherRole(Long id, UserRole role) {
        log.info("Updating teacher role: {} -> {}", id, role);

        Teacher teacher = findTeacherById(id);
        teacher.setRole(role);
        teacher = teacherRepository.save(teacher);

        return teacherMapper.toResponse(teacher);
    }
}
