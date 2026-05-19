package com.sb3.dto.idp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdpGeneralInfoResponse {
    private Long id;
    private Long studentId;
    private Integer version;
    private String status;
    private String content;
    private String originalContent;
    private String edits;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant approvedAt;
}
