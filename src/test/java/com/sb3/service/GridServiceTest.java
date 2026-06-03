package com.sb3.service;

import com.sb3.constant.GridStatus;
import com.sb3.dto.grid.GridRequest;
import com.sb3.entity.grid.Grid;
import com.sb3.entity.grid.SkillScore;
import com.sb3.entity.skill.Skill;
import com.sb3.entity.student.Student;
import com.sb3.exception.NotFoundException;
import com.sb3.repository.GridRepository;
import com.sb3.repository.SkillRepository;
import com.sb3.repository.StudentRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.sb3.constant.ErrorMessages.GRID_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GridService")
class GridServiceTest {

    @Mock private GridRepository gridRepository;
    @Mock private SkillRepository skillRepository;
    @Mock private StudentRepository studentRepository;

    @InjectMocks
    private GridService service;

    @Nested
    @DisplayName("patchGrid")
    class PatchGrid {

        @Test
        @DisplayName("should update existing scores in DRAFT grid")
        void updateExistingScores() {
            GridRequest request = new GridRequest();
            request.setStudentId(1L);
            request.setTutorId(10L);
            request.setScores(Map.of("A1", 3));

            Skill skill = new Skill();
            skill.setCode("A1");

            SkillScore existingScore = new SkillScore();
            existingScore.setSkill(skill);
            existingScore.setScore(1);

            Grid draftGrid = new Grid();
            draftGrid.setId(5L);
            draftGrid.setScores(new ArrayList<>(List.of(existingScore)));

            when(gridRepository.findByStudentIdAndGridStatus(1L, GridStatus.DRAFT))
                    .thenReturn(Optional.of(draftGrid));
            when(skillRepository.findAll()).thenReturn(List.of(skill));

            service.patchGrid(request);

            assertThat(existingScore.getScore()).isEqualTo(3);
            verify(gridRepository).save(draftGrid);
        }

        @Test
        @DisplayName("should add new scores to DRAFT grid")
        void addNewScores() {
            GridRequest request = new GridRequest();
            request.setStudentId(1L);
            request.setScores(Map.of("A1", 2));

            Skill skill = new Skill();
            skill.setCode("A1");

            Grid draftGrid = new Grid();
            draftGrid.setId(5L);
            draftGrid.setScores(new ArrayList<>());

            when(gridRepository.findByStudentIdAndGridStatus(1L, GridStatus.DRAFT))
                    .thenReturn(Optional.of(draftGrid));
            when(skillRepository.findAll()).thenReturn(List.of(skill));

            service.patchGrid(request);

            assertThat(draftGrid.getScores()).hasSize(1);
            assertThat(draftGrid.getScores().getFirst().getScore()).isEqualTo(2);
            verify(gridRepository).save(draftGrid);
        }

        @Test
        @DisplayName("should create new DRAFT grid if none exists")
        void createNewGridWhenNoDraft() {
            GridRequest request = new GridRequest();
            request.setStudentId(1L);
            request.setTutorId(10L);
            request.setScores(Map.of());

            Student student = new Student();
            student.setId(1L);

            when(gridRepository.findByStudentIdAndGridStatus(1L, GridStatus.DRAFT))
                    .thenReturn(Optional.empty());
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
            when(skillRepository.findAll()).thenReturn(List.of());

            service.patchGrid(request);

            verify(gridRepository).save(any(Grid.class));
        }

        @Test
        @DisplayName("should skip unknown skill codes")
        void skipUnknownSkills() {
            GridRequest request = new GridRequest();
            request.setStudentId(1L);
            request.setScores(Map.of("UNKNOWN", 5));

            Grid draftGrid = new Grid();
            draftGrid.setId(5L);
            draftGrid.setScores(new ArrayList<>());

            when(gridRepository.findByStudentIdAndGridStatus(1L, GridStatus.DRAFT))
                    .thenReturn(Optional.of(draftGrid));
            when(skillRepository.findAll()).thenReturn(List.of());

            service.patchGrid(request);

            assertThat(draftGrid.getScores()).isEmpty();
        }

        @Test
        @DisplayName("should throw NotFoundException when student not found for new grid")
        void studentNotFoundForNewGrid() {
            GridRequest request = new GridRequest();
            request.setStudentId(99L);
            request.setScores(Map.of());

            when(gridRepository.findByStudentIdAndGridStatus(99L, GridStatus.DRAFT))
                    .thenReturn(Optional.empty());
            when(studentRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.patchGrid(request))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("setStatus")
    class SetStatus {

        @Test
        @DisplayName("should update grid status")
        void success() {
            Grid grid = new Grid();
            grid.setId(1L);
            when(gridRepository.findById(1L)).thenReturn(Optional.of(grid));

            service.setStatus(1L, GridStatus.DONE);

            assertThat(grid.getGridStatus()).isEqualTo(GridStatus.DONE);
            verify(gridRepository).save(grid);
        }

        @Test
        @DisplayName("should throw RuntimeException when grid not found")
        void gridNotFound() {
            when(gridRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.setStatus(1L, GridStatus.DONE))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage(GRID_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("getGridById")
    class GetGridById {

        @Test
        @DisplayName("should return grid when found")
        void found() {
            Grid grid = new Grid();
            grid.setId(1L);
            when(gridRepository.findById(1L)).thenReturn(Optional.of(grid));

            Grid result = service.getGridById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw NotFoundException when not found")
        void notFound() {
            when(gridRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getGridById(1L))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getAllGridByStudentId")
    class GetAllGridByStudentId {

        @Test
        @DisplayName("should return list of grids for student")
        void success() {
            Grid grid1 = new Grid();
            grid1.setId(1L);
            Grid grid2 = new Grid();
            grid2.setId(2L);
            when(gridRepository.findAllByStudentId(1L)).thenReturn(List.of(grid1, grid2));

            List<Grid> result = service.getAllGridByStudentId(1L);

            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("should return empty list when no grids")
        void emptyList() {
            when(gridRepository.findAllByStudentId(1L)).thenReturn(List.of());

            List<Grid> result = service.getAllGridByStudentId(1L);

            assertThat(result).isEmpty();
        }
    }
}