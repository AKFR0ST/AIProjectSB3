package com.sb3.mapper;

import com.sb3.dto.student.StudentRequest;
import com.sb3.dto.student.StudentResponse;
import com.sb3.entity.student.Student;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentResponse toDto(Student student);

    Student toEntity(StudentRequest request);

    @Mapping(target = "problemBehaviors", ignore = true)
    @Mapping(target = "selfStimulatoryBehaviors", ignore = true)
    void updateEntity(@MappingTarget Student student, StudentRequest request);
}
