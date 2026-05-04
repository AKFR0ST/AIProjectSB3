package com.sb2.service;

import com.sb2.client.GigaChatClient;
import com.sb2.dto.llm.LlmRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GigaChatAPIService {

    @Autowired
    private GigaChatClient gigaChatClient;

    public String textToTextRequest(LlmRequest llmRequest) {
        return gigaChatClient.gigaChatTextToTextRequest(llmRequest);
    }
}
