package com.sb2.controller;

import com.sb2.dto.student.StudentRequest;
import com.sb2.dto.student.StudentResponse;
import com.sb2.service.StudentService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    @PostMapping
    public StudentResponse create(HttpServletRequest raw, @RequestBody StudentRequest request) {

        System.out.println(request);
        System.out.println(request.getClass());

        return service.create(request);
    }

    @GetMapping("/{id}")
    public StudentResponse get(@PathVariable Long id) {
        return service.get(id);
    }

//    @PostMapping
//    public Map<String, Object> test(@RequestBody Map<String, Object> body) {
//        return body;
//    }
}
