package com.sb3.controller;

import com.sb3.dto.llm.LlmRequest;
import com.sb3.entity.llm.LLMServices;
import com.sb3.interfaces.LLMInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/llm")
@Tag(name = "LLM", description = "API для запросов к GigaChat")
@RequiredArgsConstructor
public class LLMController {
    private final LLMInterface llmInterface;

    @Value("${general.llm.default}")
    private LLMServices llmDefault;

    @Operation(summary = "Отправить текстовый запрос к GigaChat")
    @PostMapping("/generate")
    public String llmRequest(@RequestBody LlmRequest request) {
        return llmInterface.sendTextToTextRequest(request, llmDefault);
    }
}
