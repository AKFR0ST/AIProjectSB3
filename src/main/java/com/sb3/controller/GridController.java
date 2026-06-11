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
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.Optional;

import com.sb3.dto.common.ListResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/grids")
@Tag(name = "Grid", description = "API для создания анкет учеников")
@RequiredArgsConstructor
public class GridController {

    private final GridService gridService;
    private final GridRepository gridRepository;
    private final GridMapper gridMapper;

    @Operation(summary = "Получить актуальную анкету по id ученика")
    @GetMapping("/current/student/{studentId}")
    public ResponseEntity<GridResponse> getActualGridByStudentId(@PathVariable Long studentId) {
        Optional<Grid> editableGridOpt = gridRepository.findByStudentIdAndGridStatus(studentId, GridStatus.DRAFT);

        if (editableGridOpt.isPresent()) {
            Grid grid = editableGridOpt.get();
            return ResponseEntity.ok(gridMapper.toResponse(grid));
        } else {
            GridResponse response = new GridResponse();
            response.setStudentId(studentId);
            response.setGridStatus(GridStatus.DONE);
            response.setScores(Collections.emptyMap());
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Передать данные по анкете. Если актуальной анкеты нет, то создать новую.")
    @PostMapping
    public ResponseEntity<Void> patchGrid(@RequestBody GridRequest request) {
        gridService.patchGrid(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение грида по его id")
    @GetMapping("/{id}")
    public ResponseEntity<GridResponse> getGridById(@PathVariable Long id) {
        return ResponseEntity.ok(gridMapper.toResponse(gridService.getGridById(id)));
    }

    @Operation(summary = "Получение списка гридов по id ученика с пагинацией")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ListResponse<GridResponse>> getGridsByStudentId(
            @PathVariable Long studentId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Grid> gridPage = gridService.getGridsByStudentId(studentId, pageable);

        ListResponse<GridResponse> response = ListResponse.<GridResponse>builder()
                .items(gridPage.getContent().stream()
                        .map(gridMapper::toResponse)
                        .toList())
                .total(gridPage.getTotalElements())
                .page(gridPage.getNumber())
                .size(gridPage.getSize())
                .totalPages(gridPage.getTotalPages())
                .first(gridPage.isFirst())
                .last(gridPage.isLast())
                .build();

        return ResponseEntity.ok(response);
    }
}
