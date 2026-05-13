package com.sb2.controller;

import com.sb2.dto.student.StudentRequest;
import com.sb2.dto.student.StudentResponse;
import com.sb2.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
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
    public List<StudentResponse> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Получить карточку по id ученика")
    @GetMapping("/{id}")
    public StudentResponse get(@PathVariable Long id) {
        return service.get(id);
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
