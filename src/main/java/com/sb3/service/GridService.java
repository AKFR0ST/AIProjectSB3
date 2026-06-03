package com.sb3.service;

import com.sb3.constant.ErrorMessages;
import com.sb3.constant.GridStatus;
import com.sb3.dto.grid.GridRequest;
import com.sb3.entity.grid.Grid;
import com.sb3.entity.skill.Skill;
import com.sb3.entity.grid.SkillScore;
import com.sb3.entity.student.Student;
import com.sb3.exception.NotFoundException;
import com.sb3.repository.GridRepository;
import com.sb3.repository.SkillRepository;
import com.sb3.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.sb3.constant.ErrorMessages.STUDENT_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class GridService {

    private final GridRepository gridRepository;
    private final SkillRepository skillRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public void patchGrid(GridRequest request) {
        Grid grid = gridRepository
                .findByStudentIdAndGridStatus(request.getStudentId(), GridStatus.DRAFT)
                .orElseGet(() -> createNewGrid(request));

        Map<String, SkillScore> existingScores = grid.getScores().stream()
                .collect(Collectors.toMap(
                        ss -> ss.getSkill().getCode(),
                        Function.identity()
                ));

        Map<String, Skill> skillMap = skillRepository.findAll().stream()
                .collect(Collectors.toMap(Skill::getCode, Function.identity()));

        for (Map.Entry<String, Integer> entry : request.getScores().entrySet()) {
            String code = entry.getKey();
            Integer newScore = entry.getValue();

            SkillScore existing = existingScores.get(code);
            if (existing != null) {
                existing.setScore(newScore);
            } else {
                Skill skill = skillMap.get(code);
                if (skill == null) {
                    log.warn(ErrorMessages.SKILL_WITH_CODE_NOT_FOUND_SKIPPING, code);
                    continue;
                }
                grid.getScores().add(createSkillScore(grid, skill, newScore));
            }
        }

        gridRepository.save(grid);
    }

    private Grid createNewGrid(GridRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new NotFoundException(STUDENT_NOT_FOUND + request.getStudentId()));

        Grid grid = new Grid();
        grid.setStudent(student);
        grid.setTutorId(request.getTutorId());
        grid.setCreatedAt(LocalDateTime.now());
        grid.setGridStatus(GridStatus.DRAFT);
        return grid;
    }


    public void setStatus(Long gridId, GridStatus gridStatus) {
        log.info(ErrorMessages.SETTING_GRID_STATUS_TO, gridId, gridStatus);
        Grid grid = gridRepository.findById(gridId).orElseThrow(() -> new RuntimeException(ErrorMessages.GRID_NOT_FOUND));
        grid.setGridStatus(gridStatus);
        gridRepository.save(grid);
    }

    public Grid getGridById(Long id) {
        return gridRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.GRID_NOT_FOUND + id));
    }

    public List<Grid> getAllGridByStudentId(Long id) {
        return gridRepository.findAllByStudentId(id);
    }

    private SkillScore createSkillScore(Grid grid, Skill skill, Integer score) {
        SkillScore ss = new SkillScore();
        ss.setGrid(grid);
        ss.setSkill(skill);
        ss.setScore(score);
        return ss;
    }
}
