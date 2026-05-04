package com.sb2.service;

import com.sb2.dto.SkillImportDto;
import com.sb2.entity.Category;
import com.sb2.entity.Skill;
import com.sb2.repository.CategoryRepository;
import com.sb2.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final SkillRepository skillRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {

        if (skillRepository.count() > 0) {
            return; // дабы не дублировать
        }

        Category category = categoryRepository.findByCode("A")
                .orElseGet(() -> {
                    Category c = new Category();
                    c.setCode("A");
                    c.setName("Cooperation");
                    return categoryRepository.save(c);
                });

        InputStream is = new ClassPathResource("ablls/skills.json").getInputStream();

        List<SkillImportDto> dtos = objectMapper.readValue(
                is,
                new TypeReference<List<SkillImportDto>>() {}
        );

        List<Skill> skills = dtos.stream().map(dto -> {
            Skill skill = new Skill();
            skill.setCode(dto.getCode());
            skill.setMaxScore(dto.getMaxScore());
            skill.setTask(dto.getTask());
            skill.setTaskDesc(dto.getTaskDesc());
            skill.setQuestion(dto.getQuestion());
            skill.setExample(dto.getExample());
            skill.setCriteria(dto.getCriteria());
            skill.setComment(dto.getComment());
            skill.setCategory(category);
            return skill;
        }).toList();

        skillRepository.saveAll(skills);
    }
}
