package com.sb2.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class GridRequest {

    private Long childId;

    private Long tutorId;

    /**
     * skillCode -> score
     * Example:
     * {
     *   "A1": 2,
     *   "A2": 3
     * }
     */
    private Map<String, Integer> scores;
}
