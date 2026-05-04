package com.sb2.dto.grid;

import com.sb2.constant.GridStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

@Data
public class GridResponse {

    @Schema(description = "Student ID", example = "123")
    private Long studentId;

    @Schema(description = "Tutor ID", example = "456")
    private Long tutorId;

    @Schema(description = "Current grid status", example = "DRAFT")
    private GridStatus gridStatus;

    @Schema(
            description = "Map of skill/criteria to score",
            example = "{\"A1\": 1, \"A2\": 0}"
    )
    private Map<String, Integer> scores;
}
