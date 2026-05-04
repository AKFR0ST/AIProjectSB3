package com.sb2.controller;

import com.sb2.dto.llm.LlmRequest;
import com.sb2.service.GigaChatAPIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/llm")
@Tag(name = "LLM", description = "API для запросов к GigaChat")
@RequiredArgsConstructor
public class LLMController {
    private final GigaChatAPIService gigaChatAPIService;

    @Operation(summary = "Отправить текстовый запрос к GigaChat")
    @GetMapping("/generate")
    public String llmRequest(@RequestBody LlmRequest request) {
        return gigaChatAPIService.textToTextRequest(request);
    }
}
