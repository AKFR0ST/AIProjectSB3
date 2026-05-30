package com.sb3.entity.skill;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MasteryCriterion {

    private String evaluationUnit;

    private Boolean consecutive;

    private Integer requiredCount;

    private Integer percentage;

    private String responseType;

    private Integer acrossTargets;
}