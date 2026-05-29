package com.sb3.dto.idp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GenerateGeneralInfoResponseDto {

    private String status;

    /**
     * AI-generated general_info object.
     * Храним как Object/Map, т.к. схема может эволюционировать.
     */
    private Object draft;

    private String message;
}
