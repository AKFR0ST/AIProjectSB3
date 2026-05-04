package com.sb2.controller;

import com.sb2.entity.Category;
import com.sb2.entity.Skill;
import com.sb2.repository.CategoryRepository;
import com.sb2.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/entity")
@RequiredArgsConstructor
public class EntityController {

    private final SkillRepository skillRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping("/skills")
    public List<Skill> getSkills() {
        return skillRepository.findAll();
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
