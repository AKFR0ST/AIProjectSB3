package com.sb3.controller;

import com.sb3.constant.GridStatus;
import com.sb3.dto.grid.GridRequest;
import com.sb3.dto.grid.GridResponse;
import com.sb3.entity.grid.Grid;
import com.sb3.mapper.GridMapper;
import com.sb3.repository.GridRepository;
import com.sb3.service.GridService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/grids")
@Tag(name = "Grid", description = "API для создания анкет учеников")
@RequiredArgsConstructor
public class GridController {

    private final GridService gridService;
    private final GridRepository gridRepository;
    private final GridMapper gridMapper;

    @Operation(summary = "Получить актуальную анкету по id ученика")
    @GetMapping("/{studentId}")
    public GridResponse getActualGridByStudentId(@PathVariable Long studentId) {
        Optional<Grid> editableGridOpt = gridRepository.findByStudentIdAndGridStatus(studentId, GridStatus.DRAFT);

        if (editableGridOpt.isPresent()) {
            Grid grid = editableGridOpt.get();
            return gridMapper.toResponse(grid);
        } else {
            GridResponse response = new GridResponse();
            response.setStudentId(studentId);
            response.setGridStatus(GridStatus.DONE);
            response.setScores(Collections.emptyMap());
            return response;
        }
    }

//    private GridResponse mapToGridResponse(Grid grid) {  //  TODO в mapper
//        GridResponse response = new GridResponse();
//        response.setCreatedAt(grid.getCreatedAt());
//        response.setId(grid.getId());
//        response.setStudentId(grid.getStudent().getId());
//        response.setTutorId(grid.getTutorId());
//        response.setGridStatus(grid.getGridStatus());
//        Map<String, Integer> scoresMap = grid.getScores().stream()
//                .collect(Collectors.toMap(
//                        skillScore -> skillScore.getSkill().getCode(),
//                        SkillScore::getScore
//                ));
//        response.setScores(scoresMap);
//        return response;
//    }

    @Operation(summary = "Передать данные по анкете. Если актуальной анкеты нет, то создать новую.")
    @PostMapping("/")
    public void patchGrid(@RequestBody GridRequest request) {
        gridService.patchGrid(request);
    }

    @Operation(summary = "Получение грида по его id")
    @GetMapping("/temp/{id}")
    public GridResponse getGridById(@PathVariable Long id) {
        return gridMapper.toResponse(gridService.getGridById(id));
    }

    @Operation(summary = "Получение списка гридов по id ученика")
    @GetMapping("/temp/student/{id}")
    public List<GridResponse> getGridByStudentId(@PathVariable Long id) {
        return gridService.getAllGridByStudentId(id).stream().map(gridMapper::toResponse)
                .toList();
    }
}
