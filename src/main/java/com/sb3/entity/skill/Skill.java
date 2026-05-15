package com.sb3.entity.skill;

import com.sb3.entity.category.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Код", example = "A1")
    private String code; // A1, A2

    @Column(nullable = false)
    @Schema(description = "Максимальный балл ", example = "2")
    private Integer maxScore;

    @Column(nullable = false)
    @Schema(description = "Задание", example = "Берет предложенный приятный стимул")
    private String task;

    @Column(length = 2000)
    @Schema(description = "Описание задачи", example = "Когда ребенку предлагают известный для него приятный предмет или вид деятельности, ребенок берет подкрепляющий предмет или занимается предложенной активностью")
    private String taskDesc;

    @Column(length = 2000)
    @Schema(description = "Вопрос", example = "Если вы держите знакомые ребенку приятные стимулы, возьмет ли он их?")
    private String question;

    @Column(length = 2000)
    @Schema(description = "Пример", example = "Берет из рук взрослого конфету и ест её")
    private String example;

    @Column(length = 2000)
    @Schema(description = "Критерий", example = "2 ребенок каждый раз берет приятный предмет в течение 3 секунд; 1= берет предмет не каждый раз или берет позже, чем в пределах 3 секунд")
    private String criteria;

    @Column(length = 2000)
    @Schema(description = "Комментарий", example = "Изменено")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
