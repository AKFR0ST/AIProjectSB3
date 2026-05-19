package com.sb3.entity.grid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateExercisesRequest {
    private Long studentId;
    private Map<String, Integer> scores;
}
