package com.sb3.dto.idp;

import com.sb3.constant.TrialMode;
import com.sb3.constant.TrialResult;
import lombok.*;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrialRequest {
    private String target;
    private TrialMode mode;
    private TrialResult result;
}
