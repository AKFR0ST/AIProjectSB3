package com.sb3.service;

import com.sb3.constant.ErrorMessages;
import com.sb3.constant.GridStatus;
import com.sb3.dto.exercise.ExercisesRequestDto;
import com.sb3.dto.exercise.ExercisesResponseDto;
import com.sb3.dto.idp.GenerateGeneralInfoRequestDto;
import com.sb3.dto.idp.GenerateGeneralInfoResponseDto;
import com.sb3.entity.grid.Grid;
import com.sb3.entity.grid.SkillScore;
import com.sb3.entity.task.Task;
import com.sb3.entity.task.TaskStatus;
import com.sb3.exception.NotFoundException;
import com.sb3.exception.TaskNotFoundException;
import com.sb3.mapper.SkillExerciseMapper;
import com.sb3.repository.GridRepository;
import com.sb3.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.sb3.constant.GridStatus.PROCESSING;
import static com.sb3.constant.LoggerMessages.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;
    private final GridRepository gridRepository;
    private final GridService gridService;
    private final ObjectMapper objectMapper;
    private final IdpService idpService;
    private final RestClient restClient;
    private final SkillExerciseMapper skillExerciseMapper;

    public Task createTask(Long gridId) {

        log.info(CREATING_TASK_FOR_GRID, gridId);

        if (Objects.isNull(gridId)) {
            throw new IllegalArgumentException(
                    ErrorMessages.GRID_ID_MUST_NOT_BE_EMPTY
            );
        }

        LocalDateTime now = LocalDateTime.now();

        gridService.setStatus(gridId, GridStatus.PROCESSING);

        return repository.save(
                Task.builder()
                        .gridId(gridId)
                        .status(TaskStatus.CREATED)
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
        );
    }

    @Async
    public void processTask(UUID taskId) {

        log.info(START_PROCESSING_TASK, taskId);

        Task task = repository.findById(taskId)
                .orElseThrow();

        try {

            task.setStatus(TaskStatus.PROCESSING);

            repository.save(task);

            Grid grid = gridRepository.findByIdWithScores(task.getGridId())
                    .orElseThrow(() ->
                            new NotFoundException(
                                    "Grid not found: " + task.getGridId()
                            )
                    );

            ExercisesRequestDto exercisesRequest = buildRequest(grid);

            log.info(
                    "Sending exercises request to agent: {}",
                    objectMapper.writeValueAsString(exercisesRequest)
            );

            String responseJsonExercise = restClient   //  TODO При переходе на Camel вернуть старую версию.
                    .post()
                    .uri("/agent/generate-exercises")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(exercisesRequest)
                    .retrieve()
                    .body(String.class);

            log.info("Agent response body: {}", responseJsonExercise);

            ExercisesResponseDto exercisesResponseDto = objectMapper.readValue(responseJsonExercise, ExercisesResponseDto.class);

            if (exercisesResponseDto == null
                    || "error".equals(exercisesResponseDto.getStatus())) {

                throw new RuntimeException(
                        "Agent exercises error: "
                                + (
                                exercisesResponseDto != null
                                        ? exercisesResponseDto.getMessage()
                                        : "null response"
                        )
                );
            }

            String jsonResponseGeneralInfo = restClient
                    .post()
                    .uri("/agent/generate-general-info")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            GenerateGeneralInfoRequestDto.builder()
                                    .studentId(grid.getStudent().getId())
                                    .build()
                    )
                    .retrieve()
                    .body(String.class);

            GenerateGeneralInfoResponseDto generateGeneralInfoResponseDto = objectMapper.readValue(jsonResponseGeneralInfo, GenerateGeneralInfoResponseDto.class);

            if (generateGeneralInfoResponseDto == null
                    || "error".equals(generateGeneralInfoResponseDto.getStatus())) {

                throw new RuntimeException(
                        "Agent general-info error: "
                                + (
                                generateGeneralInfoResponseDto != null
                                        ? generateGeneralInfoResponseDto.getMessage()
                                        : "null response"
                        )
                );
            }

            String resultJson = objectMapper.writeValueAsString(
                    exercisesResponseDto.getDraft()
            );

            log.info("Exercises from agent: {}", objectMapper.writeValueAsString(exercisesResponseDto.getDraft()));

            task.setResult(resultJson);  //  TODO убрать из модели - эти данные сложатся в IDP

            if (exercisesResponseDto.getDraft() != null) {

                idpService.saveExercisesFromAgent(
                        grid,
                        skillExerciseMapper.toEntityList(
                                exercisesResponseDto.getDraft()
                        ),
                        generateGeneralInfoResponseDto.getDraft()
                );
            }

            gridService.setStatus(grid.getId(), GridStatus.DONE);
            task.setStatus(TaskStatus.DONE);

            log.info(TASK_PROCESSED_SUCCESSFULLY, taskId);

        } catch (Exception e) {

            log.error(
                    ERROR_PROCESSING_TASK,
                    taskId,
                    e.getMessage(),
                    e
            );

            gridService.setStatus(task.getGridId(), GridStatus.DRAFT);
            task.setStatus(TaskStatus.FAILED);

            task.setError(e.getMessage());
        }

        finally {
            task.setUpdatedAt(LocalDateTime.now());

            repository.save(task);
        }

    }

    private ExercisesRequestDto buildRequest(Grid grid) {

        Map<String, Integer> scores = new HashMap<>();

        for (SkillScore score : grid.getScores()) {
            scores.put(
                    score.getSkill().getCode(),
                    score.getScore()
            );
        }

        return ExercisesRequestDto.builder()
                .studentId(grid.getStudent().getId())
                .scores(scores)
                .build();
    }

    public Task getTask(UUID id) {

        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}