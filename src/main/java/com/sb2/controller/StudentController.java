package com.sb2.controller;

import com.sb2.dto.student.StudentRequest;
import com.sb2.dto.student.StudentResponse;
import com.sb2.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    @PostMapping
    public StudentResponse create(@RequestBody StudentRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public StudentResponse get(@PathVariable Long id) {
        return service.get(id);
    }
}
