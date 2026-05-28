package com.sb3.dto.idp.context;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class IdpGeneralContextDto {

    private EnvironmentDto environment;
    private RecommendationsDto recommendations;

    private String preferredReinforcers;

    private List<ReinforcementSystemDto> reinforcementSystems;

    private ErrorCorrectionProcedureDto errorCorrectionProcedure;
}
