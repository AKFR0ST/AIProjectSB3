package com.sb3.dto.exercise;

import com.sb3.dto.skill.SkillExerciseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExercisesResponseDto {
    private String status;
    private List<SkillExerciseDto> draft;
    private String message;
}
