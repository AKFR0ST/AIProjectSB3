package com.sb3.service;

import com.sb3.client.CloudRuClient;
import com.sb3.dto.llm.LlmRequest;
import com.sb3.entity.llm.CloudRuModels;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CloudRuAPIService {

    private final CloudRuClient cloudRuClient;

    public String textToTextRequest(LlmRequest llmRequest, CloudRuModels cloudRuModels) {
        return cloudRuClient.cloudRuTextToTextRequest(llmRequest, cloudRuModels);
    }
}
