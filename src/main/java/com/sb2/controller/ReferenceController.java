package com.sb2.controller;

import com.sb2.dto.GridRequest;
import com.sb2.entity.Category;
import com.sb2.entity.Grid;
import com.sb2.entity.Skill;
import com.sb2.repository.CategoryRepository;
import com.sb2.repository.SkillRepository;
import com.sb2.service.GridService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReferenceController {

    private final SkillRepository skillRepository;
    private final CategoryRepository categoryRepository;
    private final GridService gridService;

    @GetMapping("/skills")
    public List<Skill> getSkills() {
        return skillRepository.findAll();
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping("/grids")
    public Grid createGrid(@RequestBody GridRequest request) {
        return gridService.createGrid(request);
    }
}
