package com.sb3.entity.skill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SkillExercise {
    private String skillCode;
    private String scale;
    private String name;
    private String goal;
    private String procedure;
    private String prompt;
    private String masteryCriterion;
    private String dataCollection;
    private List<String> exercises;
}
