package com.sb2.service;

import com.sb2.constant.ErrorMessages;
import com.sb2.entity.Task;
import com.sb2.entity.TaskStatus;
import com.sb2.exception.TaskNotFoundException;
import com.sb2.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.sb2.constant.LoggerMessages.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    public Task createTask(String fileName) {
        log.info(CREATING_TASK_FOR_FILE, fileName);
        if (Objects.isNull(fileName) || fileName.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.FILE_NAME_MUST_NOT_BE_EMPTY);
        }

        LocalDateTime now = LocalDateTime.now();

        return repository.save(
                Task.builder()
                        .inputText(fileName)
                        .status(TaskStatus.CREATED)
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
        );
    }

    @Async
    public void processTask(UUID taskId) {
        log.info(START_PROCESSING_TASK, taskId);
        Task task = repository.findById(taskId).orElseThrow();

        try {
            task.setStatus(TaskStatus.PROCESSING);
            repository.save(task);

            // TODO тут стоит расположить интеграцию с сервисом парсинга
            Thread.sleep(3000);
            String result = "Processed: " + task.getInputText();

            task.setResult(result);
            task.setStatus(TaskStatus.DONE);

            log.info(TASK_PROCESSED_SUCCESSFULLY, taskId);

        } catch (Exception e) {
            log.error(ERROR_PROCESSING_TASK, taskId, e.getMessage(), e);
            task.setStatus(TaskStatus.FAILED);
            task.setError(e.getMessage());
        }

        task.setUpdatedAt(LocalDateTime.now());
        repository.save(task);
    }

    public Task getTask(UUID id) {
        return repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

}
