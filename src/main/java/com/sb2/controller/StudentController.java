package com.sb2.controller;

import com.sb2.dto.student.StudentRequest;
import com.sb2.dto.student.StudentResponse;
import com.sb2.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
@Tag(name = "Student", description = "API для редактирования карточек учеников")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    @Operation(summary = "Создать новую карточку")
    @PostMapping
    public StudentResponse create(@RequestBody StudentRequest request) {
        return service.create(request);
    }

    @Operation(summary = "Получить карточку по id ученика")
    @GetMapping("/{id}")
    public StudentResponse get(@PathVariable Long id) {
        return service.get(id);
    }
}
