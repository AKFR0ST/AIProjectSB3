package com.sb2.controller;

import com.sb2.constant.GridStatus;
import com.sb2.dto.grid.GridRequest;
import com.sb2.dto.grid.GridResponse;
import com.sb2.entity.grid.Grid;
import com.sb2.entity.skill.SkillScore;
import com.sb2.repository.GridRepository;
import com.sb2.service.GridService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/grid")
@Tag(name = "Grid", description = "API для создания анкет учеников")
@RequiredArgsConstructor
public class GridController {

    private final GridService gridService;
    private final GridRepository gridRepository;

    @Operation(summary = "Получить актуальную анкету по id ученика")
    @GetMapping("/actual/{studentId}")
    public GridResponse getActualGridByStudentId(@PathVariable Long studentId) {
        Optional<Grid> editableGridOpt = gridRepository.findByStudentIdAndGridStatus(studentId, GridStatus.DRAFT);

        if (editableGridOpt.isPresent()) {
            Grid grid = editableGridOpt.get();
            return mapToGridResponse(grid);
        } else {
            GridResponse response = new GridResponse();
            response.setStudentId(studentId);
            response.setGridStatus(GridStatus.DONE);
            response.setScores(Collections.emptyMap());
            return response;
        }
    }

    private GridResponse mapToGridResponse(Grid grid) {  //  TODO напрашивается mapstruct
        GridResponse response = new GridResponse();
        response.setStudentId(grid.getStudentId());
        response.setTutorId(grid.getTutorId());
        response.setGridStatus(grid.getGridStatus());
        Map<String, Integer> scoresMap = grid.getScores().stream()
                .collect(Collectors.toMap(
                        skillScore -> skillScore.getSkill().getTask(),
                        SkillScore::getScore
                ));
        response.setScores(scoresMap);
        return response;
    }

    @Operation(summary = "Передать данные по анкете. Если актуальной анкеты нет, то создать новую.")
    @PostMapping("/edit")
    public void patchGrid(@RequestBody GridRequest request) {
        gridService.patchGrid(request);
    }
}
