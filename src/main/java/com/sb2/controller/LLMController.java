package com.sb2.controller;

import com.sb2.service.GigaChatAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/llm")
@RequiredArgsConstructor
public class LLMController {
    private final GigaChatAPIService gigaChatAPIService;

    @GetMapping("/{role}/{request}")
    public String llmRequest(@PathVariable String role, @PathVariable String request) {
        return gigaChatAPIService.textToTextRequest(role, request);
    }
}
