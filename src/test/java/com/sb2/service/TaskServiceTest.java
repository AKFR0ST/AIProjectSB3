package com.sb2.service;

import com.sb2.entity.task.Task;
import com.sb2.entity.task.TaskStatus;
import com.sb2.exception.TaskNotFoundException;
import com.sb2.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private GridService gridService;

    @InjectMocks
    private TaskService service;

    @Test
    void create_task_success() {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setGridId(1L);
        task.setStatus(TaskStatus.CREATED);

        when(repository.save(any(Task.class)))
                .thenReturn(task);

        Task result = service.createTask(1L);

        assertNotNull(result);
        assertEquals(1L, result.getGridId());
        assertEquals(TaskStatus.CREATED, result.getStatus());

        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    void should_fail_when_fileName_is_empty() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.createTask(null)
        );

        assertEquals("gridId must not be empty", ex.getMessage());
    }

    @Test
    void should_fail_when_fileName_is_null() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.createTask(null)
        );

        assertEquals("gridId must not be empty", ex.getMessage());
    }

    @Test
    void should_return_task_when_exists() {
        UUID id = UUID.randomUUID();

        Task task = new Task();
        task.setId(id);

        when(repository.findById(id))
                .thenReturn(Optional.of(task));

        Task result = service.getTask(id);

        assertEquals(id, result.getId());
    }

    @Test
    void should_throw_exception_when_task_not_found() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> service.getTask(id));
    }

    @Test
    void should_process_task_successfully() {
        UUID id = UUID.randomUUID();

        Task task = new Task();
        task.setId(id);
        task.setGridId(1L);
        task.setStatus(TaskStatus.CREATED);

        when(repository.findById(id))
                .thenReturn(Optional.of(task));

        when(repository.save(any()))
                .thenReturn(task);

        service.processTask(id);

        assertEquals(TaskStatus.DONE, task.getStatus());
        assertNotNull(task.getResult());

        verify(repository, atLeastOnce()).save(any());
    }

}