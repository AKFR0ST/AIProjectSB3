package com.sb3.service;

import com.sb3.dto.student.StudentRequest;
import com.sb3.entity.student.Student;
import com.sb3.dto.student.StudentResponse;
import com.sb3.mapper.StudentMapper;
import com.sb3.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    public List<StudentResponse> getAll() {
        List<Student> students = repository.findAll();
        return students.stream().map(studentMapper::toDto).toList();
    }

    public StudentResponse get(Long id) {
        return repository.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    public StudentResponse put(Long id, StudentRequest request) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        studentMapper.updateEntity(student, request);

        student = repository.save(student);
        return studentMapper.toDto(student);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        }
        repository.deleteById(id);
    }
}
