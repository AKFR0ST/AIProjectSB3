package com.sb3.controller;

import com.sb3.dto.student.StudentRequest;
import com.sb3.dto.student.StudentResponse;
import com.sb3.dto.student.StudentShortResponse;
import com.sb3.dto.student.StudentsListResponse;
import com.sb3.entity.student.Student;
import com.sb3.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Students", description = "API для редактирования карточек учеников")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    @Operation(summary = "Создать новую карточку")
    @PostMapping
    public StudentResponse create(@RequestBody StudentRequest request) {
        return service.create(request);
    }

    @Operation(summary = "Получить все карточки учеников")
    @GetMapping
    public StudentsListResponse getAll() {
        return service.getAll();
    }

    @Operation(summary = "Получить карточку по id ученика")
    @GetMapping("/{id}")
    public StudentResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @Operation(summary = "Получить список учеников с пагинацией (краткая информация)")
    @GetMapping("/list")
    public ResponseEntity<Page<StudentShortResponse>> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.getShortList(PageRequest.of(page, size)));
    }

    @Operation(summary = "Обновить карточку ученика по id")
    @PutMapping("/{id}")
    public StudentResponse put(@PathVariable Long id, @RequestBody StudentRequest request) {
        return service.put(id, request);
    }

    @Operation(summary = "Удалить карточку ученика по id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
