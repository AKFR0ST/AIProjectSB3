package com.sb3.mapper;

import com.sb3.dto.student.StudentRequest;
import com.sb3.dto.student.StudentResponse;
import com.sb3.dto.student.StudentShortResponse;
import com.sb3.entity.student.Relative;
import com.sb3.entity.student.Student;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentResponse toDto(Student student);

    Student toEntity(StudentRequest request);

    @Mapping(target = "problemBehaviors", ignore = true)
    @Mapping(target = "selfStimulatoryBehaviors", ignore = true)
    void updateEntity(@MappingTarget Student student, StudentRequest request);

    @Mapping(source = "personalInfo.birthDate", target = "birthDate")
    @Mapping(source = "generalInfo.diagnosisAndHealthStatus", target = "diagnosis")
    @Mapping(source = "schoolInfo.type", target = "schoolType")
    @Mapping(source = "personalInfo.relatives", target = "relativeName", qualifiedByName = "firstRelativeName")
    @Mapping(source = "personalInfo.relatives", target = "relativePhone", qualifiedByName = "firstRelativePhone")
    StudentShortResponse toShortResponse(Student student);

    @Named("firstRelativeName")
    default String firstRelativeName(List<Relative> relatives) {
        return (relatives != null && !relatives.isEmpty()) ? relatives.getFirst().getFullName() : null;
    }

    @Named("firstRelativePhone")
    default String firstRelativePhone(List<Relative> relatives) {
        return (relatives != null && !relatives.isEmpty()) ? relatives.getFirst().getPhone() : null;
    }
}
