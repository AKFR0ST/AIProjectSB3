package com.sb3.interfaces;

import com.sb3.dto.llm.LlmRequest;
import com.sb3.entity.llm.LLMServices;

public interface LLMInterface {
    String sendTextToTextRequest(LlmRequest llmRequest, LLMServices llmServices);
}
