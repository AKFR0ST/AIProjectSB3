package com.sb3.entity.skill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasteryCriterion {
    private String evaluationUnit;
    private Boolean consecutive;
    private Integer requiredCount;
    private Integer percentage;
    private String responseType;
    private Integer acrossTargets;
}
