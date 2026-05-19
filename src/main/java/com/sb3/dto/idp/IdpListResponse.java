package com.sb3.dto.idp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdpListResponse {
    private List<IdpGeneralInfoResponse> items;
    private Long total;
}
