package com.sb3.dto.idp;

import lombok.*;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrialRequest {
    private String grade; // "С", "П", "О", "Н"
    private Instant trialDate;
    private String note;
}
