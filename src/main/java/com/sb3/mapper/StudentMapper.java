package com.sb3.mapper;

import com.sb3.dto.student.StudentRequest;
import com.sb3.dto.student.StudentResponse;
import com.sb3.entity.student.Student;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ACCESSOR_ONLY)
public interface StudentMapper {

    StudentResponse toDto(Student student);

    Student toEntity(StudentRequest request);

    void updateEntity(@MappingTarget Student student, StudentRequest request);
}
