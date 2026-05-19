package com.sb3.service;

import com.sb3.dto.idp.*;
import com.sb3.entity.idp.IdpExercises;
import com.sb3.entity.idp.IdpGeneralInfo;
import com.sb3.entity.student.Student;
import com.sb3.exception.NotFoundException;
import com.sb3.mapper.IdpMapper;
import com.sb3.repository.IdpExercisesRepository;
import com.sb3.repository.IdpGeneralInfoRepository;
import com.sb3.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IdpService {

    private final IdpGeneralInfoRepository generalInfoRepository;
    private final IdpExercisesRepository exercisesRepository;
    private final StudentRepository studentRepository;
    private final IdpMapper mapper;

    // General Info
    public IdpGeneralInfoResponse createGeneralInfo(IdpGeneralInfoRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new NotFoundException("Студент не найден"));

        IdpGeneralInfo entity = mapper.toEntity(request);
        entity.setStudent(student);
        entity.setStatus("draft");
        entity.setVersion(1);

        return mapper.toGeneralInfoResponse(generalInfoRepository.save(entity));
    }

    public IdpListResponse getGeneralInfoByStudent(Long studentId) {
        List<IdpGeneralInfoResponse> items = mapper.toGeneralInfoResponseList(
                generalInfoRepository.findByStudentIdOrderByVersionDesc(studentId));
        return new IdpListResponse(items, (long) items.size());
    }

    public IdpGeneralInfoResponse getGeneralInfo(Long id) {
        return mapper.toGeneralInfoResponse(findGeneralInfoById(id));
    }

    public IdpGeneralInfoResponse updateGeneralInfo(Long id, IdpGeneralInfoRequest request) {
        IdpGeneralInfo entity = findGeneralInfoById(id);

        // Сохраняем оригинал перед обновлением
        entity.setOriginalContent(entity.getContent());
        entity.setEdits(request.getContent()); // или merge edits

        entity.setContent(request.getContent());
        entity.setVersion(entity.getVersion() + 1);

        return mapper.toGeneralInfoResponse(generalInfoRepository.save(entity));
    }

    public IdpGeneralInfoResponse approveGeneralInfo(Long id) {
        IdpGeneralInfo entity = findGeneralInfoById(id);
        entity.setStatus("approved");
        entity.setApprovedAt(Instant.now());
        return mapper.toGeneralInfoResponse(generalInfoRepository.save(entity));
    }

    public void deleteGeneralInfo(Long id) {
        generalInfoRepository.delete(findGeneralInfoById(id));
    }

    // Exercises
    public IdpExercisesResponse createExercise(IdpExercisesRequest request) {
        IdpGeneralInfo idpGeneralInfo = findGeneralInfoById(request.getIdpGeneralInfoId());

        IdpExercises entity = mapper.toEntity(request);
        entity.setIdpGeneralInfo(idpGeneralInfo);
        entity.setStatus("draft");

        return mapper.toExercisesResponse(exercisesRepository.save(entity));
    }

    public List<IdpExercisesResponse> getExercisesByIdpGeneralInfoId(Long idpGeneralInfoId) {
        return mapper.toExercisesResponseList(
                exercisesRepository.findByIdpGeneralInfoId(idpGeneralInfoId));
    }

    public IdpExercisesResponse updateExercise(Long id, IdpExercisesRequest request) {
        IdpExercises entity = findExerciseById(id);

        entity.setOriginalContent(entity.getContent());
        entity.setContent(request.getContent());
        entity.setSkillCodes(request.getSkillCodes());

        return mapper.toExercisesResponse(exercisesRepository.save(entity));
    }

    public IdpExercisesResponse approveExercise(Long id) {
        IdpExercises entity = findExerciseById(id);
        entity.setStatus("approved");
        return mapper.toExercisesResponse(exercisesRepository.save(entity));
    }

    public void deleteExercise(Long id) {
        exercisesRepository.delete(findExerciseById(id));
    }

    private IdpGeneralInfo findGeneralInfoById(Long id) {
        return generalInfoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("IDP запись не найдена"));
    }

    private IdpExercises findExerciseById(Long id) {
        return exercisesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Упражнение не найдено"));
    }
}
