package com.sb2.controller;

import com.sb2.constant.GridStatus;
import com.sb2.dto.GridRequest;
import com.sb2.dto.GridResponse;
import com.sb2.entity.Category;
import com.sb2.entity.Grid;
import com.sb2.entity.Skill;
import com.sb2.entity.SkillScore;
import com.sb2.repository.CategoryRepository;
import com.sb2.repository.GridRepository;
import com.sb2.repository.SkillRepository;
import com.sb2.service.GridService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReferenceController {

    private final SkillRepository skillRepository;
    private final CategoryRepository categoryRepository;
    private final GridService gridService;
    private final GridRepository gridRepository;

    @GetMapping("/grids/actual/{studentId}")
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

    @GetMapping("/skills")
    public List<Skill> getSkills() {
        return skillRepository.findAll();
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping("/grids")
    public void patchGrid(@RequestBody GridRequest request) {
        gridService.patchGrid(request);
    }
}
