package com.sb3.dto.skill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillResponse {
    private Long id;
    private String code;
    private Integer maxScore;
    private String task;
    private String taskDesc;
    private String question;
    private String example;
    private String criteria;
    private String comment;
    private Long categoryId;
    private String categoryName;
}
