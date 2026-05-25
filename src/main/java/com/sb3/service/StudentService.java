package com.sb3.service;

import com.sb3.dto.student.StudentRequest;
import com.sb3.dto.student.StudentsListResponse;
import com.sb3.entity.student.PersonalInfo;
import com.sb3.entity.student.Student;
import com.sb3.dto.student.StudentResponse;
import com.sb3.mapper.StudentMapper;
import com.sb3.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository repository;
    private final StudentMapper studentMapper;

    public StudentResponse create(StudentRequest request) {
        Student student = studentMapper.toEntity(request);

        // TODO тут тоже вернуть отлетевший mapstuct
        if (student.getPersonalInfo() != null) student.getPersonalInfo().setId(null);
        if (student.getMedicalInfo() != null) student.getMedicalInfo().setId(null);
        if (student.getAbaHistory() != null) student.getAbaHistory().setId(null);
        if (student.getCommunication() != null) student.getCommunication().setId(null);
        if (student.getSocialAndPlay() != null) student.getSocialAndPlay().setId(null);
        if (student.getLearningSkills() != null) student.getLearningSkills().setId(null);
        if (student.getBehavior() != null) {
            student.getBehavior().setId(null);
            if (student.getBehavior().getDangerousBehavior() != null) {
                student.getBehavior().getDangerousBehavior().setId(null);
            }
        }
        if (student.getMotivation() != null) {
            student.getMotivation().setId(null);
            if (student.getMotivation().getPreferred() != null)
                student.getMotivation().getPreferred().setId(null);
        }
        if (student.getDailyRoutine() != null) student.getDailyRoutine().setId(null);
        if (student.getSchoolInfo() != null) student.getSchoolInfo().setId(null);
        if (student.getPreliminarySkillAssessment() != null) student.getPreliminarySkillAssessment().setId(null);
        if (student.getSocialAndPlay() != null) {
            student.getSocialAndPlay().setId(null);
            if (student.getSocialAndPlay().getPeerInteractions() != null)
                student.getSocialAndPlay().getPeerInteractions().setId(null);
            if (student.getSocialAndPlay().getGroupBehavior() != null)
                student.getSocialAndPlay().getGroupBehavior().setId(null);
        }

        if (student.getProblemBehaviors() != null)
            student.getProblemBehaviors().forEach(pb -> pb.setId(null));
        if (student.getSelfStimulatoryBehaviors() != null)
            student.getSelfStimulatoryBehaviors().forEach(sb -> sb.setId(null));

        student = repository.save(student);
        return studentMapper.toDto(student);
    }

    public StudentsListResponse getAll() {
        List<StudentResponse> items = repository.findAll()
                .stream()
                .map(studentMapper::toDto)
                .toList();
        return new StudentsListResponse(items, (long) items.size());
    }

    public StudentResponse get(Long id) {
        return repository.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    public StudentResponse put(Long id, StudentRequest request) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        studentMapper.updateEntity(student, request);

        // TODO вернуть mapstruct
        if (request.getProblemBehaviors() != null) {
            student.getProblemBehaviors().clear();
            student.getProblemBehaviors().addAll(request.getProblemBehaviors());
        }
        if (request.getSelfStimulatoryBehaviors() != null) {
            student.getSelfStimulatoryBehaviors().clear();
            student.getSelfStimulatoryBehaviors().addAll(request.getSelfStimulatoryBehaviors());
        }

        student = repository.save(student);
        return studentMapper.toDto(student);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        }
        repository.deleteById(id);
    }
}
