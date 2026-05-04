package com.sb2.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GigaChatChoice {
    @JsonProperty("message")
    GigaChatRequestMessage requestMessage;
    @JsonProperty("index")
    Integer index;
    @JsonProperty("finish_reason")
    String finishReason;
}
