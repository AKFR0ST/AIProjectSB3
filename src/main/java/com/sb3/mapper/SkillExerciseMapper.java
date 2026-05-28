package com.sb3.mapper;

import com.sb3.dto.skill.ExercisesDto;
import com.sb3.dto.skill.MasteryCriterionDto;
import com.sb3.dto.skill.SkillExerciseDto;
import com.sb3.entity.skill.Exercises;
import com.sb3.entity.skill.MasteryCriterion;
import com.sb3.entity.skill.SkillExercise;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SkillExerciseMapper {

    SkillExercise toEntity(SkillExerciseDto dto);

    List<SkillExercise> toEntityList(List<SkillExerciseDto> dtoList);

    Exercises toEntity(ExercisesDto dto);

    MasteryCriterion toEntity(MasteryCriterionDto dto);
}
