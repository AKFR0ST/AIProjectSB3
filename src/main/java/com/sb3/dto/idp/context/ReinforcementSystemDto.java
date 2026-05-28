package com.sb3.dto.idp.context;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReinforcementSystemDto {

    private String name;

    private String type;

    private String description;
}
