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
    private Long gridId;
    private Integer version;
    private String status;
    private Object content;
    private Object originalContent;
    private Object edits;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant approvedAt;
}
