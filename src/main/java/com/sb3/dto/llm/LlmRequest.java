package com.sb3.dto.llm;

import lombok.Data;

@Data
public class LlmRequest {
    private String role;
    private String prompt;
}
