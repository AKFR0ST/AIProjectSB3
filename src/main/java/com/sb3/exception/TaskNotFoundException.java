package com.sb3.exception;

import java.util.UUID;

import static com.sb3.constant.ErrorMessages.TASK_NOT_FOUND;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(UUID id) {
        super(TASK_NOT_FOUND + id);
    }
}
