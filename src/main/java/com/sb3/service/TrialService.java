package com.sb3.service;

import com.sb3.dto.idp.TrialRequest;
import com.sb3.dto.idp.TrialResponse;
import com.sb3.entity.idp.*;
import com.sb3.exception.NotFoundException;
import com.sb3.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TrialService {

    private final TrialRepository trialRepository;
    private final IdpExercisesRepository exercisesRepository;

    public List<TrialResponse> getTrials(Long exerciseId, Instant from, Instant to) {
        findExerciseById(exerciseId);
        List<Trial> trials;
        if (from != null && to != null) {
            trials = trialRepository.findByExerciseIdAndTrialDateBetweenOrderByCreatedAtDesc(exerciseId, from, to);
        } else {
            trials = trialRepository.findByExerciseIdOrderByCreatedAtDesc(exerciseId);
        }
        return trials.stream().map(this::toResponse).toList();
    }

    public TrialResponse createTrial(Long exerciseId, TrialRequest request) {
        IdpExercises exercise = findExerciseById(exerciseId);

        Trial trial = Trial.builder()
                .exercise(exercise)
                .grade(request.getGrade())
                .trialDate(request.getTrialDate() != null ? request.getTrialDate() : Instant.now())
                .note(request.getNote())
                .build();

        return toResponse(trialRepository.save(trial));
    }

    public void deleteTrial(Long exerciseId, UUID trialId) {
        findExerciseById(exerciseId);
        Trial trial = trialRepository.findById(trialId)
                .orElseThrow(() -> new NotFoundException("Проба не найдена: " + trialId));
        if (!trial.getExercise().getId().equals(exerciseId)) {
            throw new NotFoundException("Проба не относится к упражнению: " + exerciseId);
        }
        trialRepository.delete(trial);
    }

    private IdpExercises findExerciseById(Long id) {
        return exercisesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Упражнение не найдено: " + id));
    }

    private TrialResponse toResponse(Trial trial) {
        return TrialResponse.builder()
                .id(trial.getId())
                .exerciseId(trial.getExercise().getId())
                .grade(trial.getGrade())
                .trialDate(trial.getTrialDate())
                .note(trial.getNote())
                .createdAt(trial.getCreatedAt())
                .build();
    }
}
