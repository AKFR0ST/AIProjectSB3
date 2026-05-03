package com.sb2.service;

import com.sb2.dto.GridRequest;
import com.sb2.entity.Grid;
import com.sb2.entity.Skill;
import com.sb2.entity.SkillScore;
import com.sb2.repository.GridRepository;
import com.sb2.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GridService {

    private final GridRepository gridRepository;
    private final SkillRepository skillRepository;

    Optional<Grid> findByIdAndStatus(Long id, String status) {
        return null;
    }

    public Grid getGridById(Long gridId) {
        return gridRepository.findByGridId(gridId);
    }

    public Grid createGrid(GridRequest request) {

        Grid grid = new Grid();
        grid.setStudentId(request.getStudentId());
        grid.setTutorId(request.getTutorId());
        grid.setCreatedAt(LocalDateTime.now());

        List<Skill> skills = skillRepository.findAll();

        for (Skill skill : skills) {

            Integer score = request.getScores().get(skill.getCode());

            if (score == null) {
                continue;
            }

            SkillScore ss = new SkillScore();
            ss.setSkill(skill);
            ss.setScore(score);
            ss.setGrid(grid);

            grid.getScores().add(ss);
        }

        return gridRepository.save(grid);
    }
}
