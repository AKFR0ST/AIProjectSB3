package com.sb3.dto.idp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpExercisesRequest {
    private Long idpGeneralInfoId;
    private List<String> skillCodes;
    private String content;
}
