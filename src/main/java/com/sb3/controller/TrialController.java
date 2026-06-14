package com.sb3.controller;

import com.sb3.dto.idp.TrialRequest;
import com.sb3.dto.idp.TrialResponse;
import com.sb3.service.TrialService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/idp/exercises/{exercise_id}/trials")
@Tag(name = "Trials", description = "Управление пробами упражнений")
@RequiredArgsConstructor
public class TrialController {

    private final TrialService service;

    @GetMapping
    public ResponseEntity<List<TrialResponse>> getTrials(
            @PathVariable("exercise_id") Long exerciseId,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Instant from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Instant to) {
        return ResponseEntity.ok(service.getTrials(exerciseId, from, to));
    }

    @PostMapping
    public ResponseEntity<TrialResponse> createTrial(
            @PathVariable("exercise_id") Long exerciseId,
            @RequestBody TrialRequest request) {
        return ResponseEntity.status(201).body(service.createTrial(exerciseId, request));
    }

    @DeleteMapping("/{trial_id}")
    public ResponseEntity<Void> deleteTrial(
            @PathVariable("exercise_id") Long exerciseId,
            @PathVariable("trial_id") UUID trialId) {
        service.deleteTrial(exerciseId, trialId);
        return ResponseEntity.noContent().build();
    }
}
