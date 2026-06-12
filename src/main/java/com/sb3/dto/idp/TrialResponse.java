package com.sb3.dto.idp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrialResponse {
    private UUID id;
    private Long exerciseId;
    private String grade;
    private Instant trialDate;
    private String note;
    private Instant createdAt;
}
