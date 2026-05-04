package com.sb2.dto.grid;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class GridRequest {

    @Schema(description = "Student ID", example = "123")
    private Long studentId;

    @Schema(description = "Tutor ID", example = "456")
    private Long tutorId;

    @Schema(
            description = "Map of skill/criteria to score",
            example = "{\"A1\": 1, \"A2\": 0}"
    )
    private Map<String, Integer> scores;
}
