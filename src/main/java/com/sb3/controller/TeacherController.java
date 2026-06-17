package com.sb3.controller;

import com.sb3.constant.TeacherStatus;
import com.sb3.constant.UserRole;
import com.sb3.dto.student.StudentShortResponse;
import com.sb3.dto.teacher.TeacherRequest;
import com.sb3.dto.teacher.TeacherResponse;
import com.sb3.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

import com.sb3.dto.common.ListResponse;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import java.util.List;


@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @Operation(summary = "Получить список преподавателей с пагинацией")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ListResponse<TeacherResponse>> getAllTeachers(
            @PageableDefault(size = 20, sort = "lastName", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<TeacherResponse> page = teacherService.getAllTeachersOnly(pageable);

        ListResponse<TeacherResponse> response = ListResponse.<TeacherResponse>builder()
                .items(page.getContent())
                .total(page.getTotalElements())
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Получить преподавателя по ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<TeacherResponse> getTeacherById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @Operation(summary = "Создать преподавателя (только ADMIN)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherResponse> createTeacher(@Valid @RequestBody TeacherRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teacherService.createTeacher(request));
    }

    @Operation(summary = "Обновить преподавателя (только ADMIN)")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherResponse> updateTeacher(
            @PathVariable Long id,
            @Valid @RequestBody TeacherRequest request) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, request));
    }

    @Operation(summary = "Обновить статус преподавателя (только ADMIN)")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherResponse> updateTeacherStatus(
            @PathVariable Long id,
            @RequestParam TeacherStatus status) {
        return ResponseEntity.ok(teacherService.updateTeacherStatus(id, status));
    }

    @Operation(summary = "Обновить роль преподавателя (только ADMIN)")
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherResponse> updateTeacherRole(
            @PathVariable Long id,
            @RequestParam UserRole role) {
        return ResponseEntity.ok(teacherService.updateTeacherRole(id, role));
    }

    @Operation(summary = "Удалить преподавателя (только ADMIN)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить студентов преподавателя")
    @GetMapping("/{id}/students")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<StudentShortResponse>> getTeacherStudents(@PathVariable Long id) {
        List<StudentShortResponse> students = teacherService.getTeacherStudents(id);
        return ResponseEntity.ok(students);
    }
}
