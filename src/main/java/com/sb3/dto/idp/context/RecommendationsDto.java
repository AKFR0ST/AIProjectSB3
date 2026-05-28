package com.sb3.dto.idp.context;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecommendationsDto {

    private String generalMethods;

    private String behaviorManagement;

    private String reviewAndReinforcement;

    private String additionalRecommendations;
}
