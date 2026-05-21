package com.sb3.service;

import com.sb3.constant.ErrorMessages;
import com.sb3.entity.grid.GenerateExercisesRequest;
import com.sb3.entity.grid.GenerateExercisesResponse;
import com.sb3.entity.grid.Grid;
import com.sb3.entity.grid.SkillScore;
import com.sb3.entity.skill.SkillExercise;
import com.sb3.entity.task.Task;
import com.sb3.entity.task.TaskStatus;
import com.sb3.exception.NotFoundException;
import com.sb3.exception.TaskNotFoundException;
import com.sb3.repository.GridRepository;
import com.sb3.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.*;

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
    private final RestClient restClient = RestClient.builder()
            .baseUrl("http://ai-agent:8089")
            .build();

    public Task createTask(Long gridId) {
        log.info(CREATING_TASK_FOR_GRID, gridId);
        if (Objects.isNull(gridId)) {
            throw new IllegalArgumentException(ErrorMessages.GRID_ID_MUST_NOT_BE_EMPTY);
        }

        LocalDateTime now = LocalDateTime.now();

        gridService.markDone(gridId);

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
    public void processTask(UUID taskId) {  // TODO на рефакторинг
        log.info(START_PROCESSING_TASK, taskId);
        Task task = repository.findById(taskId).orElseThrow();

        try {
            task.setStatus(TaskStatus.PROCESSING);
            repository.save(task);

            Grid grid = gridRepository.findByIdWithScores(task.getGridId())
                    .orElseThrow(() -> new NotFoundException("Grid not found: " + task.getGridId()));

            GenerateExercisesRequest request = buildRequest(grid);

            GenerateExercisesResponse agentResponse = restClient
                    .post()
                    .uri("/agent/generate-exercises")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(GenerateExercisesResponse.class);

            if (agentResponse == null || "error".equals(agentResponse.getStatus())) {
                throw new RuntimeException("Agent error: " + agentResponse.getMessage());
            }

            String resultJson = objectMapper.writeValueAsString(agentResponse.getDraft());
            task.setResult(resultJson);
            task.setStatus(TaskStatus.DONE);

            log.info(TASK_PROCESSED_SUCCESSFULLY, taskId);

            if (agentResponse.getDraft() != null) {

                    idpService.saveExercisesFromAgent(grid.getStudent().getId(), agentResponse.getDraft());

            }

        } catch (Exception e) {
            log.error(ERROR_PROCESSING_TASK, taskId, e.getMessage(), e);
            task.setStatus(TaskStatus.FAILED);
            task.setError(e.getMessage());
        }

        task.setUpdatedAt(LocalDateTime.now());
        repository.save(task);
    }

    private GenerateExercisesRequest buildRequest(Grid grid) {
        Map<String, Integer> scores = new HashMap<>();
        for (SkillScore score : grid.getScores()) {
            scores.put(score.getSkill().getCode(), score.getScore());
        }

        return GenerateExercisesRequest.builder()
                .studentId(grid.getStudent().getId())
                .scores(scores)
                .build();
    }

    public Task getTask(UUID id) {
        return repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

}
