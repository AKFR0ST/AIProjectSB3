package com.sb3.exception;

public class AgentResponseException extends RuntimeException {

    public AgentResponseException(String message) {
        super(message);
    }

    public AgentResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
