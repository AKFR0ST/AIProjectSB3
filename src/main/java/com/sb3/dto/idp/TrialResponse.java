package com.sb3.dto.idp;

import com.sb3.constant.TrialMode;
import com.sb3.constant.TrialResult;
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
    private String target;
    private TrialMode mode;
    private TrialResult result;
    private Instant createdAt;
}
