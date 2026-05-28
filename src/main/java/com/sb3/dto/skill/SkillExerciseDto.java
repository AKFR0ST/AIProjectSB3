package com.sb3.dto.skill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SkillExerciseDto {

    private List<String> skillCodes;

    private String category;

    private String name;

    private String goal;

    private String procedure;

    private String instruction;

    private String prompt;

    private String errorCorrection;

    private String materials;

    private MasteryCriterionDto masteryCriterion;

    private ExercisesDto exercises;
}
