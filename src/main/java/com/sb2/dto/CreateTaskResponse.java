package com.sb2.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Ответ при создании задачи")
public record CreateTaskResponse(

        @Schema(description = "ID задачи", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,

        @Schema(description = "Статус задачи", example = "CREATED")
        String status
) {}
