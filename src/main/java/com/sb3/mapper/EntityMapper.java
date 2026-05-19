package com.sb3.mapper;

import com.sb3.dto.Category.CategoryResponse;
import com.sb3.dto.skill.SkillResponse;
import com.sb3.entity.category.Category;
import com.sb3.entity.skill.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    SkillResponse toSkillDto(Skill skill);

    List<SkillResponse> toSkillDtoList(List<Skill> skills);

    CategoryResponse toCategoryDto(Category category);

    List<CategoryResponse> toCategoryDtoList(List<Category> categories);
}
