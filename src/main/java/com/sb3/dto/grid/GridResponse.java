package com.sb3.dto.grid;

import com.sb3.constant.GridStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class GridResponse {

    @Schema(description = "Grid ID", example = "7")
    private Long id;

    @Schema(description = "Student ID", example = "123")
    private Long studentId;

    @Schema(description = "Tutor ID", example = "456")
    private Long tutorId;

    @Schema(description = "Current grid status", example = "DRAFT")
    private GridStatus gridStatus;

    @Schema(description = "Created at", example = "2007-12-03T10:15:30")
    private LocalDateTime createdAt;

    @Schema(
            description = "Map of skill/criteria to score",
            example = "{\"A1\": 1, \"A2\": 0}"
    )
    private Map<String, Integer> scores;
}
