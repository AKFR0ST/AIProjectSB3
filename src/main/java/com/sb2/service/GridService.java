package com.sb2.service;

import com.sb2.constant.GridStatus;
import com.sb2.dto.GridRequest;
import com.sb2.entity.Grid;
import com.sb2.entity.Skill;
import com.sb2.entity.SkillScore;
import com.sb2.repository.GridRepository;
import com.sb2.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GridService {

    private final GridRepository gridRepository;
    private final SkillRepository skillRepository;

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
        Grid grid = new Grid();
        grid.setStudentId(request.getStudentId());
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
}
