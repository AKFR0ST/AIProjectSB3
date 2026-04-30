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

@Service
@RequiredArgsConstructor
public class GridService {

    private final GridRepository gridRepository;
    private final SkillRepository skillRepository;

    public Grid createGrid(GridRequest request) {

        Grid grid = new Grid();
        grid.setChildId(request.getChildId());
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
