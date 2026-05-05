package com.sb2.service;

import com.sb2.client.GigaChatClient;
import com.sb2.dto.llm.LlmRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GigaChatAPIService {

    private final GigaChatClient gigaChatClient;

    public String textToTextRequest(LlmRequest llmRequest) {
        return gigaChatClient.gigaChatTextToTextRequest(llmRequest);
    }
}
