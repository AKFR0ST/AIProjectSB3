package com.sb3.client;

import com.sb3.dto.llm.CloudRu.CloudRuRequest;
import com.sb3.dto.llm.CloudRu.CloudRuResponse;
import com.sb3.dto.llm.LlmRequest;
import com.sb3.entity.llm.CloudRuModels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static com.sb3.constant.GigaChatConstants.*;


@Slf4j
@Component
public class CloudRuClient {

    private static final String CHAT_COMPLETIONS_URI =
            "/v1/chat/completions";

    private final String baseUrl;
    private final String authKey;

    private RestClient baseRestClient;

    public CloudRuClient(
            @Value("${cloudru.base.url}") String baseUrl,
            @Value("${cloudru.authorization.key}") String authKey
    ) {
        this.baseUrl = baseUrl;
        this.authKey = authKey;
        updateBaseClient();
    }

    private void updateBaseClient() {

        baseRestClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + authKey)
                .requestInterceptor((request, body, execution) -> {

                    log.info("=== CLOUD.RU REQUEST ===");
                    log.info("URI: {}", request.getURI());
                    log.info("METHOD: {}", request.getMethod());
                    log.info("HEADERS: {}", request.getHeaders());

                    if (body != null) {
                        log.info("BODY: {}", new String(body, StandardCharsets.UTF_8));
                    }

                    ClientHttpResponse response = execution.execute(request, body);

                    log.info("=== CLOUD.RU RESPONSE ===");
                    log.info("STATUS: {} {}", response.getStatusCode(), response.getStatusText());
                    log.info("HEADERS: {}", response.getHeaders());

                    return response;
                })
                .build();
    }

    public String cloudRuTextToTextRequest(
            LlmRequest llmRequest,
            CloudRuModels cloudRuModels
    ) {

        ArrayList<CloudRuRequest.Message> messages = new ArrayList<>();

        if (!llmRequest.getRole().isEmpty()) {
            messages.add(
                    CloudRuRequest.Message.builder()
                            .role("system")
                            .content(llmRequest.getRole())
                            .build()
            );
        }

        messages.add(
                CloudRuRequest.Message.builder()
                        .role("user")
                        .content(llmRequest.getPrompt())
                        .build()
        );

        CloudRuRequest ruRequest = CloudRuRequest.builder()
                .model(cloudRuModels.getTitle())
                .messages(messages)
                .build();

        log.info("CloudRuRequest object: {}", ruRequest);

        CloudRuResponse cloudRuResponse =
                executeWithRetryOnUnauthenticated(ruRequest);

        log.info("CloudRuResponse object: {}", cloudRuResponse);

        return cloudRuResponse.getChoices()
                .getFirst()
                .getMessage()
                .getContent();
    }

    private CloudRuResponse executeWithRetryOnUnauthenticated(
            CloudRuRequest requestChat
    ) {

        try {

            return baseRestClient.post()
                    .uri(CHAT_COMPLETIONS_URI)
                    .body(requestChat)
                    .retrieve()
                    .body(CloudRuResponse.class);

        } catch (HttpStatusCodeException e) {

            log.error("=== CLOUD.RU ERROR RESPONSE ===");
            log.error("STATUS: {}", e.getStatusCode());
            log.error("BODY: {}", e.getResponseBodyAsString(), e);

            throw e;
        }
    }
}
