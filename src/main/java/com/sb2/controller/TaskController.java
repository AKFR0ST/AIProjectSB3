package com.sb2.controller;

import com.sb2.dto.task.CreateTaskResponse;
import com.sb2.dto.task.ErrorResponse;
import com.sb2.dto.task.TaskResponse;
import com.sb2.entity.task.Task;
import com.sb2.entity.task.TaskStatus;
import com.sb2.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/tasks")
@Tag(name = "Task", description = "API для создания задач обработки анкет учеников")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @Operation(summary = "Создать задачу на обработку анкеты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Задача принята в обработку",
                    content = @Content(schema = @Schema(implementation = CreateTaskResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @PostMapping
    public ResponseEntity<CreateTaskResponse> createTask(@RequestParam Long gridId) {
        log.info("Received request to create task: gridId={}", gridId);
        Task task = service.createTask(gridId);
        service.processTask(task.getId());

        CreateTaskResponse response =
                new CreateTaskResponse(task.getId(), TaskStatus.CREATED.name());

        return ResponseEntity.accepted().body(response);
    }


    @Operation(summary = "Получить задачу по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Задача найдена",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "404", description = "Задача не найдена",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/{taskId}")
    public Task getTask(@PathVariable UUID taskId) {
        return service.getTask(taskId);
    }

}
