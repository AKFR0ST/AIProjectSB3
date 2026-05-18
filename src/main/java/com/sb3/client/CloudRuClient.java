package com.sb3.client;

import com.sb3.dto.llm.CloudRu.CloudRuRequest;
import com.sb3.dto.llm.CloudRu.CloudRuResponse;
import com.sb3.dto.llm.LlmRequest;
import com.sb3.entity.llm.CloudRuModels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;

import static com.sb3.constant.GigaChatConstants.*;


@Slf4j
@Component
public class CloudRuClient {

    String baseUrl;
    String authKey;

    public CloudRuClient(
            @Value("${cloudru.base.url}") String baseUrl,
            @Value("${cloudru.authorization.key}") String authKey){

        this.baseUrl = baseUrl;
        this.authKey = authKey;
        updateBaseClient();
    }

    private RestClient baseRestClient;

    private void updateBaseClient() {
        baseRestClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON)
                .defaultHeader(AUTHORIZATION, BEARER + authKey)
                .build();
    }

    public String cloudRuTextToTextRequest(LlmRequest llmRequest, CloudRuModels cloudRuModels) {

        ArrayList<CloudRuRequest.Message> messages = new ArrayList<>();

        if (!llmRequest.getRole().isEmpty()) {
            CloudRuRequest.Message roleMessage = CloudRuRequest.Message.builder()
                    .role(SYSTEM)
                    .content(llmRequest.getRole())
                    .build();
            messages.add(roleMessage);
        }

        CloudRuRequest.Message textMessage = CloudRuRequest.Message.builder()
                .role(USER)
                .content(llmRequest.getPrompt())
                .build();
        messages.add(textMessage);

        CloudRuRequest ruRequest = CloudRuRequest.builder()
                .model(String.valueOf(cloudRuModels))
                .messages(messages)
                .build();

        log.info("CloudRuRequest: {}", ruRequest);
        CloudRuResponse cloudRuResponse = executeWithRetryOnUnauthenticated(ruRequest);
        log.info("CloudRuResponse: {}", cloudRuResponse);
        return cloudRuResponse.getChoices().getFirst().getMessage().getContent();
    }

    private CloudRuResponse executeWithRetryOnUnauthenticated(CloudRuRequest requestChat) {
            return baseRestClient.post()
                    .body(requestChat)
                    .retrieve()
                    .body(CloudRuResponse.class);
    }
}
