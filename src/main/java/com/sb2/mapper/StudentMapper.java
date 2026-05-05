package com.sb2.mapper;

import com.sb2.dto.student.StudentRequest;
import com.sb2.dto.student.StudentResponse;
import com.sb2.entity.student.Student;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentResponse toDto(Student student);

    Student toEntity(StudentRequest request);

    void updateEntity(@MappingTarget Student student, StudentRequest request);
}
