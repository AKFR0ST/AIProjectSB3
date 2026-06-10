package com.sb3.service;

import com.sb3.constant.GridStatus;
import com.sb3.dto.grid.GridResponse;
import com.sb3.entity.grid.Grid;
import com.sb3.entity.grid.SkillScore;
import com.sb3.entity.skill.Skill;
import com.sb3.entity.student.Student;
import com.sb3.mapper.GridMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class GridMapperTest {

    private GridMapper gridMapper;

    @BeforeEach
    void setUp() {
        gridMapper = Mappers.getMapper(GridMapper.class);
    }

    @Test
    void shouldMapGridToResponseSuccessfully() {

        Student student = new Student();
        student.setId(10L);

        Skill skillMath = new Skill();
        skillMath.setCode("MATH");

        Skill skillEng = new Skill();
        skillEng.setCode("ENG");

        SkillScore mathScore = new SkillScore();
        mathScore.setSkill(skillMath);
        mathScore.setScore(85);

        SkillScore engScore = new SkillScore();
        engScore.setSkill(skillEng);
        engScore.setScore(92);

        Grid grid = new Grid();
        grid.setId(100L);
        grid.setStudent(student);
        grid.setTutorId(200L);
        grid.setGridStatus(GridStatus.DRAFT);
        grid.setCreatedAt(LocalDateTime.of(2024, 1, 15, 10, 30));
        grid.setScores(List.of(mathScore, engScore));


        GridResponse response = gridMapper.toResponse(grid);


        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.getId()).isEqualTo(100L),
                () -> assertThat(response.getStudentId()).isEqualTo(10L),
                () -> assertThat(response.getTutorId()).isEqualTo(200L),
                () -> assertThat(response.getGridStatus()).isEqualTo(GridStatus.DRAFT),
                () -> assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 1, 15, 10, 30))
        );

        // Проверяем маппинг scores
        Map<String, Integer> scores = response.getScores();
        assertThat(scores).hasSize(2);
        assertThat(scores).containsEntry("MATH", 85);
        assertThat(scores).containsEntry("ENG", 92);
    }

    @Test
    void shouldMapNullStudentId() {

        Grid grid = new Grid();
        grid.setId(100L);
        grid.setStudent(null);
        grid.setTutorId(200L);
        grid.setGridStatus(GridStatus.DRAFT);
        grid.setCreatedAt(LocalDateTime.now());
        grid.setScores(List.of());


        GridResponse response = gridMapper.toResponse(grid);


        assertThat(response).isNotNull();
        assertThat(response.getStudentId()).isNull();
        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getTutorId()).isEqualTo(200L);
    }

    @Test
    void shouldMapNullScores() {

        Student student = new Student();
        student.setId(10L);

        Grid grid = new Grid();
        grid.setId(100L);
        grid.setStudent(student);
        grid.setTutorId(200L);
        grid.setGridStatus(GridStatus.DRAFT);
        grid.setCreatedAt(LocalDateTime.now());
        grid.setScores(null);  // scores = null


        GridResponse response = gridMapper.toResponse(grid);

        assertThat(response).isNotNull();
        assertThat(response.getScores()).isNull();
    }

    @Test
    void shouldMapEmptyScores() {

        Student student = new Student();
        student.setId(10L);

        Grid grid = new Grid();
        grid.setId(100L);
        grid.setStudent(student);
        grid.setTutorId(200L);
        grid.setGridStatus(GridStatus.DRAFT);
        grid.setCreatedAt(LocalDateTime.now());
        grid.setScores(List.of());  // empty list

        GridResponse response = gridMapper.toResponse(grid);

        assertThat(response).isNotNull();
        assertThat(response.getScores()).isNotNull();
        assertThat(response.getScores()).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDuplicateSkillCodes() {

        Skill skill = new Skill();
        skill.setCode("MATH");

        SkillScore score1 = new SkillScore();
        score1.setSkill(skill);
        score1.setScore(85);

        SkillScore score2 = new SkillScore();
        score2.setSkill(skill);
        score2.setScore(95);

        Student student = new Student();
        student.setId(10L);

        Grid grid = new Grid();
        grid.setId(100L);
        grid.setStudent(student);
        grid.setTutorId(200L);
        grid.setGridStatus(GridStatus.DRAFT);
        grid.setCreatedAt(LocalDateTime.now());
        grid.setScores(List.of(score1, score2));

        assertThatThrownBy(() -> gridMapper.toResponse(grid))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicate key");
    }

    @Test
    void shouldMapAllGridStatuses() {

        Student student = new Student();
        student.setId(10L);

        for (GridStatus status : GridStatus.values()) {
            Grid grid = new Grid();
            grid.setId(100L);
            grid.setStudent(student);
            grid.setTutorId(200L);
            grid.setGridStatus(status);
            grid.setCreatedAt(LocalDateTime.now());
            grid.setScores(List.of());

            GridResponse response = gridMapper.toResponse(grid);

            assertThat(response.getGridStatus()).isEqualTo(status);
        }
    }

    @Test
    void shouldPreserveImmutability() {

        Skill skill = new Skill();
        skill.setCode("MATH");

        SkillScore skillScore = new SkillScore();
        skillScore.setSkill(skill);
        skillScore.setScore(85);

        Student student = new Student();
        student.setId(10L);

        Grid grid = new Grid();
        grid.setId(100L);
        grid.setStudent(student);
        grid.setTutorId(200L);
        grid.setGridStatus(GridStatus.DRAFT);
        grid.setCreatedAt(LocalDateTime.now());
        grid.setScores(List.of(skillScore));

        GridResponse response1 = gridMapper.toResponse(grid);
        GridResponse response2 = gridMapper.toResponse(grid);

        assertThat(response1).usingRecursiveComparison().isEqualTo(response2);
        assertThat(response1.getScores()).isNotSameAs(response2.getScores()); // разные объекты
    }
}
