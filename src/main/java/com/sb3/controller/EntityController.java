package com.sb3.controller;

import com.sb3.entity.category.Category;
import com.sb3.entity.skill.Skill;
import com.sb3.repository.CategoryRepository;
import com.sb3.repository.SkillRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/entities")
@Tag(name = "Entity", description = "API для управления скилами и категориями")
@RequiredArgsConstructor
public class EntityController {

    private final SkillRepository skillRepository;
    private final CategoryRepository categoryRepository;

    @Operation(summary = "Получить список скиллов")
    @GetMapping("/skills")
    public List<Skill> getSkills() {
        return skillRepository.findAll();
    }

    @Operation(summary = "Получить список категорий")
    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
