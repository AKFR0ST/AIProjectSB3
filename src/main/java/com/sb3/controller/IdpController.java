package com.sb3.controller;

import com.sb3.dto.idp.*;
import com.sb3.service.IdpService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/idp")
@Tag(name = "IDP", description = "API для управления ИПР (Индивидуальный План Развития)")
@RequiredArgsConstructor
public class IdpController {

    private final IdpService service;

    // General Info
    @PostMapping("/general")
    public ResponseEntity<IdpGeneralInfoResponse> createGeneralInfo(@RequestBody IdpGeneralInfoRequest request) {
        return ResponseEntity.status(201).body(service.createGeneralInfo(request));
    }

    @GetMapping("/general/student/{studentId}")
    public ResponseEntity<IdpListResponse> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(service.getGeneralInfoByStudent(studentId));
    }

    @GetMapping("/general/{id}")
    public ResponseEntity<IdpGeneralInfoResponse> getGeneralInfo(@PathVariable Long id) {
        return ResponseEntity.ok(service.getGeneralInfo(id));
    }

    @PutMapping("/general/{id}")
    public ResponseEntity<IdpGeneralInfoResponse> updateGeneralInfo(
            @PathVariable Long id,
            @RequestBody IdpGeneralInfoRequest request) {
        return ResponseEntity.ok(service.updateGeneralInfo(id, request));
    }

    @PutMapping("/general/{id}/approve")
    public ResponseEntity<IdpGeneralInfoResponse> approveGeneralInfo(@PathVariable Long id) {
        return ResponseEntity.ok(service.approveGeneralInfo(id));
    }

    @DeleteMapping("/general/{id}")
    public ResponseEntity<Void> deleteGeneralInfo(@PathVariable Long id) {
        service.deleteGeneralInfo(id);
        return ResponseEntity.noContent().build();
    }

    // Exercises
    @PostMapping("/exercises")
    public ResponseEntity<IdpExercisesResponse> createExercise(@RequestBody IdpExercisesRequest request) {
        return ResponseEntity.status(201).body(service.createExercise(request));
    }

    @GetMapping("/exercises/general/{idpGeneralInfoId}")
    public ResponseEntity<List<IdpExercisesResponse>> getExercises(@PathVariable Long idpGeneralInfoId) {
        return ResponseEntity.ok(service.getExercisesByIdpGeneralInfoId(idpGeneralInfoId));
    }

    @PutMapping("/exercises/{id}")
    public ResponseEntity<IdpExercisesResponse> updateExercise(
            @PathVariable Long id,
            @RequestBody IdpExercisesRequest request) {
        return ResponseEntity.ok(service.updateExercise(id, request));
    }

    @PutMapping("/exercises/{id}/approve")
    public ResponseEntity<IdpExercisesResponse> approveExercise(@PathVariable Long id) {
        return ResponseEntity.ok(service.approveExercise(id));
    }

    @DeleteMapping("/exercises/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        service.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
