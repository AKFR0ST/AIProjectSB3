package com.sb3.service;

import com.sb3.constant.GridStatus;
import com.sb3.dto.grid.GridRequest;
import com.sb3.dto.grid.GridResponse;
import com.sb3.entity.grid.Grid;
import com.sb3.entity.skill.Skill;
import com.sb3.entity.grid.SkillScore;
import com.sb3.entity.student.Student;
import com.sb3.repository.GridRepository;
import com.sb3.repository.SkillRepository;
import com.sb3.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        Map<String, SkillScore> existingScores = grid.getScores()
                .stream()
                .collect(Collectors.toMap(
                        ss -> ss.getSkill().getCode(),
                        Function.identity()
                ));

        Map<String, Skill> skillMap = skillRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        Skill::getCode,
                        Function.identity()
                ));

        for (Map.Entry<String, Integer> entry : request.getScores().entrySet()) {

            String code = entry.getKey();
            Integer newScore = entry.getValue();

            SkillScore existing = existingScores.get(code);

            if (existing != null) {
                existing.setScore(newScore);

            } else {
                Skill skill = skillMap.get(code);
                if (skill == null) continue; // TODO бросать исключение

                SkillScore newSkillScore = new SkillScore();
                newSkillScore.setGrid(grid);
                newSkillScore.setSkill(skill);
                newSkillScore.setScore(newScore);

                grid.getScores().add(newSkillScore);
            }
        }

        gridRepository.save(grid);
    }

    private Grid createNewGrid(GridRequest request) {

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Grid grid = new Grid();
        grid.setStudent(student);
        grid.setTutorId(request.getTutorId());
        grid.setCreatedAt(LocalDateTime.now());
        grid.setGridStatus(GridStatus.DRAFT);
        return grid;
    }


    public void markDone(Long gridId) {
        Grid grid = gridRepository.findById(gridId).orElseThrow();
        grid.setGridStatus(GridStatus.DONE);
        gridRepository.save(grid);
    }

    public Grid getGridById(Long id) {
        return gridRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public List<Grid> getAllGridByStudentId(Long id) {
        return gridRepository.findAllByStudentId(id);
    }
}
