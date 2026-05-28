package com.sb3.dto.idp.context;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionStructureDto {

    private String typicalWorkDuration;

    private String typicalBreakDuration;
}
