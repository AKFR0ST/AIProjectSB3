package com.sb3.entity.grid;

import com.sb3.entity.skill.SkillExercise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateExercisesResponse {
    private String status;
    private List<SkillExercise> draft;
    private String message;
}
