package com.sb3.dto.idp.context;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnvironmentDto {

    private String physicalArrangement;

    private SessionStructureDto sessionStructure;

    private String specialEquipment;
}
