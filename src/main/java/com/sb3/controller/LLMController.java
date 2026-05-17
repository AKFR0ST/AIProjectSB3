package com.sb3.controller;

import com.sb3.dto.llm.LlmRequest;
import com.sb3.service.GigaChatAPIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/llm")
@Tag(name = "LLM", description = "API для запросов к GigaChat")
@RequiredArgsConstructor
public class LLMController {
    private final GigaChatAPIService gigaChatAPIService;

    @Operation(summary = "Отправить текстовый запрос к GigaChat")
    @PostMapping("/generate")
    public String llmRequest(@RequestBody LlmRequest request) {
        return gigaChatAPIService.textToTextRequest(request);
    }
}
