package com.sb3.mapper;

import com.sb3.dto.idp.IdpExercisesResponse;
import com.sb3.dto.idp.IdpGeneralInfoResponse;
import com.sb3.entity.idp.IdpExercises;
import com.sb3.entity.idp.IdpGeneralInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IdpMapper {

    @Mapping(source = "student.id", target = "studentId")
    IdpGeneralInfoResponse toGeneralInfoResponse(IdpGeneralInfo entity);

    List<IdpGeneralInfoResponse> toGeneralInfoResponseList(List<IdpGeneralInfo> entities);

    @Mapping(source = "idpGeneralInfo.id", target = "idpGeneralInfoId")
    IdpExercisesResponse toExercisesResponse(IdpExercises entity);

    List<IdpExercisesResponse> toExercisesResponseList(List<IdpExercises> entities);
}
