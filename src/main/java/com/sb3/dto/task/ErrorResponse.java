package com.sb3.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ошибка API")
public record ErrorResponse(

        @Schema(example = "404")
        int status,

        @Schema(example = "Task not found")
        String error,

        @Schema(example = "2026-04-15T13:25:23")
        String timestamp
) {}
