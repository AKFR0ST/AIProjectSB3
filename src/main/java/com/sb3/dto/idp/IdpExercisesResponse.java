package com.sb3.dto.idp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpExercisesResponse {
    private Long id;
    private Long idpGeneralInfoId;
    private List<String> skillCodes;
    private String status;
    private String content;
    private String originalContent;
    private String edits;
    private Instant createdAt;
    private Instant updatedAt;
}
