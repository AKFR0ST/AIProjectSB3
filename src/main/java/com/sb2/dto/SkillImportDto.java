package com.sb2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillImportDto {

    @JsonProperty("КОД")
    private String code;

    @JsonProperty("БАЛЛ")
    private Integer maxScore;

    @JsonProperty("ЗАДАНИЕ")
    private String task;

    @JsonProperty("ОПИСАНИЕ ЗАДАЧИ")
    private String taskDesc;

    @JsonProperty("ВОПРОС")
    private String question;

    @JsonProperty("ПРИМЕР")
    private String example;

    @JsonProperty("КРИТЕРИЙ")
    private String criteria;

    @JsonProperty("КОММЕНТАРИЙ")
    private String comment;
}
