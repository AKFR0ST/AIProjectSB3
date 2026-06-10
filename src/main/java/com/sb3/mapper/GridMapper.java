package com.sb3.mapper;

import com.sb3.dto.grid.GridResponse;
import com.sb3.entity.grid.Grid;
import com.sb3.entity.grid.SkillScore;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface GridMapper {

    @Mapping(target = "studentId", source = "grid.student.id")
    @Mapping(target = "scores", source = "grid.scores")
    GridResponse toResponse(Grid grid);

    default Map<String, Integer> mapScores(List<SkillScore> scores) {
        if (scores == null) {
            return null;
        }
        return scores.stream()
                .collect(Collectors.toMap(
                        score -> score.getSkill().getCode(),
                        SkillScore::getScore
                ));
    }
}
