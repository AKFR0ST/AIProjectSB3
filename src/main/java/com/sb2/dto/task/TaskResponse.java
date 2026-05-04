package com.sb2.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Информация о задаче")
public record TaskResponse(
        UUID id,
        String inputText,
        String status,
        String result,
        String error
) {}
