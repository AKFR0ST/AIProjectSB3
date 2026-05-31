package com.sb3.entity.skill;

import com.sb3.dto.skill.ErrorCorrection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillExercise {

    private List<String> skillCodes;

    private String category;

    private String name;

    private String goal;

    private String procedure;

    private String instruction;

    private String prompt;

    private ErrorCorrection errorCorrection;

    private String materials;

    private MasteryCriterion masteryCriterion;

    private Exercises exercises;
}
