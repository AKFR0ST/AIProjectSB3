package com.sb3.controller;

import com.sb3.dto.Category.CategoryResponse;
import com.sb3.dto.skill.SkillResponse;
import com.sb3.mapper.EntityMapper;
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

    private final EntityMapper entityMapper;
    private final CategoryRepository categoryRepository;
    private final SkillRepository skillRepository;

    @GetMapping("/skills")
    public List<SkillResponse> getSkills() {
        return entityMapper.toSkillDtoList(skillRepository.findAll());
    }

    @Operation(summary = "Получить список категорий")
    @GetMapping("/categories")
    public List<CategoryResponse> getCategories() {
        return entityMapper.toCategoryDtoList(categoryRepository.findAll());
    }
}
