package com.sb2.exception;

import com.sb2.dto.task.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static com.sb2.constant.ErrorMessages.BAD_REQUEST_ID;
import static com.sb2.constant.ErrorMessages.TASK_NOT_FOUND_ID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(TaskNotFoundException ex) {
        log.error(TASK_NOT_FOUND_ID, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        404,
                        ex.getMessage(),
                        LocalDateTime.now().toString()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
        log.error(BAD_REQUEST_ID, ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        400,
                        ex.getMessage(),
                        LocalDateTime.now().toString()
                ));
    }
}
