package com.sb2.service;

import com.sb2.dto.student.StudentRequest;
import com.sb2.entity.student.Student;
import com.sb2.dto.student.StudentResponse;
import com.sb2.mapper.StudentMapper;
import com.sb2.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository repository;
    private final StudentMapper studentMapper;

    public StudentResponse create(StudentRequest request) {
        System.out.println(request);
        Student student = studentMapper.toEntity(request);
        student = repository.save(student);
        return studentMapper.toDto(student);
    }

    public StudentResponse get(Long id) {
        return repository.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

}
