package com.sb3.service;

import com.sb3.dto.idp.*;
import com.sb3.entity.grid.Grid;
import com.sb3.entity.idp.IdpExercises;
import com.sb3.entity.idp.IdpGeneralInfo;
import com.sb3.entity.skill.SkillExercise;
import com.sb3.entity.student.Student;
import com.sb3.exception.NotFoundException;
import com.sb3.mapper.IdpMapper;
import com.sb3.repository.IdpExercisesRepository;
import com.sb3.repository.IdpGeneralInfoRepository;
import com.sb3.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;


import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class IdpService {

    private static final Logger log = LoggerFactory.getLogger(IdpService.class);
    private final IdpGeneralInfoRepository generalInfoRepository;
    private final IdpExercisesRepository exercisesRepository;
    private final StudentRepository studentRepository;
    private final IdpMapper mapper;
    private final ObjectMapper objectMapper;
    private final IdpGeneralContextBuilder idpGeneralContextBuilder;

    // General Info
    public IdpGeneralInfoResponse createGeneralInfo(IdpGeneralInfoRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new NotFoundException("Студент не найден"));

        IdpGeneralInfo entity = new IdpGeneralInfo();
        entity.setStudent(student);
        entity.setVersion(1);
        entity.setStatus("draft");
        entity.setContent(objectMapper.writeValueAsString(request.getContent()));
        entity.setCreatedAt(Instant.now());

        return mapper.toGeneralInfoResponse(generalInfoRepository.save(entity));
    }

    public IdpListResponse getGeneralInfoByStudent(Long studentId) {
        List<IdpGeneralInfoResponse> items = generalInfoRepository
                .findByStudentIdOrderByVersionDesc(studentId)
                .stream()
                .map(entity -> {
                    IdpGeneralInfoResponse response = mapper.toGeneralInfoResponse(entity);
                    response.setContent(parseJson(entity.getContent()));
                    response.setOriginalContent(parseJson(entity.getOriginalContent()));
                    response.setEdits(parseJson(entity.getEdits()));
                    return response;
                })
                .toList();
        return new IdpListResponse(items, (long) items.size());
    }

    public IdpGeneralInfoResponse getGeneralInfo(Long id) {
        IdpGeneralInfo entity = findGeneralInfoById(id);
        IdpGeneralInfoResponse response = mapper.toGeneralInfoResponse(entity);
        response.setContent(parseJson(entity.getContent()));
        response.setOriginalContent(parseJson(entity.getOriginalContent()));
        response.setEdits(parseJson(entity.getEdits()));
        return response;
    }

    public IdpGeneralInfoResponse updateGeneralInfo(Long id, IdpGeneralInfoRequest request) {
        IdpGeneralInfo entity = findGeneralInfoById(id);

        entity.setOriginalContent(entity.getContent());
        entity.setContent(objectMapper.writeValueAsString(request.getContent()));
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
        IdpGeneralInfo info = findGeneralInfoById(request.getIdpGeneralInfoId());

        IdpExercises entity = new IdpExercises();
        entity.setIdpGeneralInfo(info);
        entity.setSkillCodes(request.getSkillCodes());
        entity.setContent(request.getContent());
        entity.setStatus("draft");
        entity.setCreatedAt(Instant.now());

        return mapper.toExercisesResponse(exercisesRepository.save(entity));
    }

    public List<IdpExercisesResponse> getExercisesByIdpGeneralInfoId(Long idpGeneralInfoId) {
        return mapper.toExercisesResponseList(
                exercisesRepository.findByIdpGeneralInfoId(idpGeneralInfoId));
    }

    public IdpExercisesResponse updateExercise(Long id, IdpExercisesRequest request) {
        IdpExercises entity = findExerciseById(id);

        if (request.getContent() != null) {
            entity.setOriginalContent(entity.getContent());
            entity.setContent(request.getContent());
        }
        if (request.getSkillCodes() != null && !request.getSkillCodes().isEmpty()) {
            entity.setSkillCodes(request.getSkillCodes());
        }

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

    private Object parseJson(String json) {
        if (json == null) return null;
        try {
            return objectMapper.readValue(json, Object.class);
        } catch (Exception e) {
            return json;
        }
    }

    @Transactional
    public void saveExercisesFromAgent(
            Grid grid,
            List<SkillExercise> exercises,
            Object generalInfoDraft
    ) {

        Student student = studentRepository.findById(grid.getStudent().getId())
                .orElseThrow(() -> new NotFoundException("Студент не найден"));

        IdpGeneralInfo generalInfo = new IdpGeneralInfo();

        generalInfo.setGrid(grid);
        generalInfo.setStudent(student);
        generalInfo.setStatus("draft");
        generalInfo.setVersion(1);
        generalInfo.setCreatedAt(Instant.now());

        Map<String, Object> contentMap = new HashMap<>();

        String code = student.getStudentCode() != null
                ? student.getStudentCode()
                : "";

        contentMap.put(
                "student",
                Map.of("code", code)
        );

        contentMap.put(
                "general_info",
                generalInfoDraft
        );

        generalInfo.setContent(writeJson(contentMap));

        generalInfo = generalInfoRepository.save(generalInfo);

        for (SkillExercise exercise : exercises) {

            IdpExercises entity = new IdpExercises();

            entity.setIdpGeneralInfo(generalInfo);

            entity.setSkillCodes(
                    exercise.getSkillCodes() != null
                            ? exercise.getSkillCodes()
                            : List.of()
            );

            log.info("!!!!!!!!!!!!!!!!MasteryCriterion: {}", exercise.getMasteryCriterion());  // TODO не забыть убрать

            entity.setContent(writeJson(exercise));

            log.info("!!!!!!!!!!!!!!!!!Content: {}", entity.getContent());

            entity.setStatus("draft");
            entity.setCreatedAt(Instant.now());

            exercisesRepository.save(entity);
        }
    }

    private String writeJson(Object obj) {  // TODO на рефакторинг
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
