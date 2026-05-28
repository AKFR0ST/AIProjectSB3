package com.sb3.entity.skill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseItem {
    private String type;
    private String description;  // или name, смотря что приходит
}
