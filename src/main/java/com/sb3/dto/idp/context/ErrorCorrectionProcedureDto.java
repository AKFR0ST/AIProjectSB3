package com.sb3.dto.idp.context;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorCorrectionProcedureDto {

    private String type;

    private String description;
}
