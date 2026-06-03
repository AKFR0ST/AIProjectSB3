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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.sb3.constant.ErrorMessages.IDP_NOT_FOUND;
import static com.sb3.constant.ErrorMessages.STUDENT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("IdpService")
class IdpServiceTest {

    @Mock private IdpGeneralInfoRepository generalInfoRepository;
    @Mock private IdpExercisesRepository exercisesRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private IdpMapper mapper;
    @Mock private ObjectMapper objectMapper;

    @InjectMocks
    private IdpService service;

    // ==================== General Info ====================

    @Nested
    @DisplayName("createGeneralInfo")
    class CreateGeneralInfo {

        @Test
        @DisplayName("should create and return IdpGeneralInfoResponse")
        void success() throws Exception {
            Student student = new Student();
            student.setId(1L);
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

            IdpGeneralInfoRequest request = new IdpGeneralInfoRequest();
            request.setStudentId(1L);
            request.setContent(Map.of("key", "value"));

            when(objectMapper.writeValueAsString(request.getContent())).thenReturn("{\"key\":\"value\"}");

            IdpGeneralInfo savedEntity = new IdpGeneralInfo();
            savedEntity.setId(10L);
            when(generalInfoRepository.save(any(IdpGeneralInfo.class))).thenReturn(savedEntity);

            IdpGeneralInfoResponse response = new IdpGeneralInfoResponse();
            response.setId(10L);
            when(mapper.toGeneralInfoResponse(savedEntity)).thenReturn(response);

            IdpGeneralInfoResponse result = service.createGeneralInfo(request);

            assertThat(result.getId()).isEqualTo(10L);
            verify(generalInfoRepository).save(any(IdpGeneralInfo.class));
        }

        @Test
        @DisplayName("should throw NotFoundException when student does not exist")
        void studentNotFound() {
            when(studentRepository.findById(1L)).thenReturn(Optional.empty());

            IdpGeneralInfoRequest request = new IdpGeneralInfoRequest();
            request.setStudentId(1L);

            assertThatThrownBy(() -> service.createGeneralInfo(request))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(STUDENT_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("getGeneralInfo")
    class GetGeneralInfo {

        @Test
        @DisplayName("should return general info by id")
        void found() {
            IdpGeneralInfo entity = new IdpGeneralInfo();
            entity.setId(1L);
            entity.setContent("{\"key\":\"value\"}");
            when(generalInfoRepository.findById(1L)).thenReturn(Optional.of(entity));

            IdpGeneralInfoResponse response = new IdpGeneralInfoResponse();
            response.setId(1L);
            when(mapper.toGeneralInfoResponse(entity)).thenReturn(response);
            when(objectMapper.readValue("{\"key\":\"value\"}", Object.class)).thenReturn(Map.of("key", "value"));

            IdpGeneralInfoResponse result = service.getGeneralInfo(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw NotFoundException when not found")
        void notFound() {
            when(generalInfoRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getGeneralInfo(1L))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(IDP_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("getGeneralInfoByStudent")
    class GetGeneralInfoByStudent {

        @Test
        @DisplayName("should return list of general info for student")
        void success() {
            IdpGeneralInfo entity = new IdpGeneralInfo();
            entity.setId(1L);
            entity.setContent("{}");
            when(generalInfoRepository.findByStudentIdOrderByVersionDesc(1L)).thenReturn(List.of(entity));

            IdpGeneralInfoResponse response = new IdpGeneralInfoResponse();
            response.setId(1L);
            when(mapper.toGeneralInfoResponse(entity)).thenReturn(response);
            when(objectMapper.readValue("{}", Object.class)).thenReturn(Map.of());

            IdpListResponse result = service.getGeneralInfoByStudent(1L);

            assertThat(result.getItems()).hasSize(1);
            assertThat(result.getTotal()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("updateGeneralInfo")
    class UpdateGeneralInfo {

        @Test
        @DisplayName("should update content and increment version")
        void success() throws Exception {
            IdpGeneralInfo entity = new IdpGeneralInfo();
            entity.setId(1L);
            entity.setContent("old");
            entity.setVersion(1);
            when(generalInfoRepository.findById(1L)).thenReturn(Optional.of(entity));

            IdpGeneralInfoRequest request = new IdpGeneralInfoRequest();
            request.setContent("new");
            when(objectMapper.writeValueAsString(request.getContent())).thenReturn("\"new\"");

            when(generalInfoRepository.save(entity)).thenReturn(entity);

            IdpGeneralInfoResponse response = new IdpGeneralInfoResponse();
            response.setId(1L);
            when(mapper.toGeneralInfoResponse(entity)).thenReturn(response);

            service.updateGeneralInfo(1L, request);

            assertThat(entity.getVersion()).isEqualTo(2);
            assertThat(entity.getOriginalContent()).isEqualTo("old");
        }
    }

    @Nested
    @DisplayName("approveGeneralInfo")
    class ApproveGeneralInfo {

        @Test
        @DisplayName("should set status to approved")
        void success() {
            IdpGeneralInfo entity = new IdpGeneralInfo();
            entity.setId(1L);
            when(generalInfoRepository.findById(1L)).thenReturn(Optional.of(entity));
            when(generalInfoRepository.save(entity)).thenReturn(entity);

            IdpGeneralInfoResponse response = new IdpGeneralInfoResponse();
            response.setStatus("approved");
            when(mapper.toGeneralInfoResponse(entity)).thenReturn(response);

            IdpGeneralInfoResponse result = service.approveGeneralInfo(1L);

            assertThat(entity.getStatus()).isEqualTo("approved");
            assertThat(entity.getApprovedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("deleteGeneralInfo")
    class DeleteGeneralInfo {

        @Test
        @DisplayName("should delete when found")
        void success() {
            IdpGeneralInfo entity = new IdpGeneralInfo();
            entity.setId(1L);
            when(generalInfoRepository.findById(1L)).thenReturn(Optional.of(entity));

            service.deleteGeneralInfo(1L);

            verify(generalInfoRepository).delete(entity);
        }

        @Test
        @DisplayName("should throw NotFoundException when not found")
        void notFound() {
            when(generalInfoRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.deleteGeneralInfo(1L))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    // ==================== Exercises ====================

    @Nested
    @DisplayName("createExercise")
    class CreateExercise {

        @Test
        @DisplayName("should create exercise linked to general info")
        void success() {
            IdpGeneralInfo info = new IdpGeneralInfo();
            info.setId(5L);
            when(generalInfoRepository.findById(5L)).thenReturn(Optional.of(info));

            IdpExercisesRequest request = new IdpExercisesRequest();
            request.setIdpGeneralInfoId(5L);
            request.setSkillCodes(List.of("A1"));
            request.setContent("{\"test\":1}");

            IdpExercises savedEntity = new IdpExercises();
            savedEntity.setId(10L);
            when(exercisesRepository.save(any(IdpExercises.class))).thenReturn(savedEntity);

            IdpExercisesResponse response = new IdpExercisesResponse();
            response.setId(10L);
            when(mapper.toExercisesResponse(savedEntity)).thenReturn(response);

            IdpExercisesResponse result = service.createExercise(request);

            assertThat(result.getId()).isEqualTo(10L);
            verify(exercisesRepository).save(any(IdpExercises.class));
        }

        @Test
        @DisplayName("should throw NotFoundException when general info not found")
        void generalInfoNotFound() {
            when(generalInfoRepository.findById(5L)).thenReturn(Optional.empty());

            IdpExercisesRequest request = new IdpExercisesRequest();
            request.setIdpGeneralInfoId(5L);

            assertThatThrownBy(() -> service.createExercise(request))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getExercisesByIdpGeneralInfoId")
    class GetExercisesByIdpGeneralInfoId {

        @Test
        @DisplayName("should return list of exercises")
        void success() {
            IdpExercises entity = new IdpExercises();
            entity.setId(1L);
            when(exercisesRepository.findByIdpGeneralInfoId(5L)).thenReturn(List.of(entity));

            IdpExercisesResponse response = new IdpExercisesResponse();
            response.setId(1L);
            when(mapper.toExercisesResponseList(List.of(entity))).thenReturn(List.of(response));

            List<IdpExercisesResponse> result = service.getExercisesByIdpGeneralInfoId(5L);

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("updateExercise")
    class UpdateExercise {

        @Test
        @DisplayName("should update content and skill codes")
        void success() {
            IdpExercises entity = new IdpExercises();
            entity.setId(1L);
            entity.setContent("old");
            when(exercisesRepository.findById(1L)).thenReturn(Optional.of(entity));
            when(exercisesRepository.save(entity)).thenReturn(entity);

            IdpExercisesRequest request = new IdpExercisesRequest();
            request.setContent("new");
            request.setSkillCodes(List.of("B1"));

            IdpExercisesResponse response = new IdpExercisesResponse();
            when(mapper.toExercisesResponse(entity)).thenReturn(response);

            service.updateExercise(1L, request);

            assertThat(entity.getContent()).isEqualTo("new");
            assertThat(entity.getOriginalContent()).isEqualTo("old");
            assertThat(entity.getSkillCodes()).containsExactly("B1");
        }

        @Test
        @DisplayName("should not update skill codes if empty")
        void emptySkillCodesNotUpdated() {
            IdpExercises entity = new IdpExercises();
            entity.setId(1L);
            entity.setSkillCodes(new ArrayList<>(List.of("A1")));
            when(exercisesRepository.findById(1L)).thenReturn(Optional.of(entity));
            when(exercisesRepository.save(entity)).thenReturn(entity);

            IdpExercisesRequest request = new IdpExercisesRequest();
            request.setSkillCodes(List.of());

            IdpExercisesResponse response = new IdpExercisesResponse();
            when(mapper.toExercisesResponse(entity)).thenReturn(response);

            service.updateExercise(1L, request);

            assertThat(entity.getSkillCodes()).containsExactly("A1"); // не изменился
        }
    }

    @Nested
    @DisplayName("approveExercise")
    class ApproveExercise {

        @Test
        @DisplayName("should set status to approved")
        void success() {
            IdpExercises entity = new IdpExercises();
            entity.setId(1L);
            when(exercisesRepository.findById(1L)).thenReturn(Optional.of(entity));
            when(exercisesRepository.save(entity)).thenReturn(entity);

            IdpExercisesResponse response = new IdpExercisesResponse();
            response.setStatus("approved");
            when(mapper.toExercisesResponse(entity)).thenReturn(response);

            IdpExercisesResponse result = service.approveExercise(1L);

            assertThat(entity.getStatus()).isEqualTo("approved");
        }
    }

    @Nested
    @DisplayName("deleteExercise")
    class DeleteExercise {

        @Test
        @DisplayName("should delete when found")
        void success() {
            IdpExercises entity = new IdpExercises();
            entity.setId(1L);
            when(exercisesRepository.findById(1L)).thenReturn(Optional.of(entity));

            service.deleteExercise(1L);

            verify(exercisesRepository).delete(entity);
        }

        @Test
        @DisplayName("should throw NotFoundException when not found")
        void notFound() {
            when(exercisesRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.deleteExercise(1L))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    // ==================== saveExercisesFromAgent ====================

    @Nested
    @DisplayName("saveExercisesFromAgent")
    class SaveExercisesFromAgent {

        @Test
        @DisplayName("should save general info and exercises from agent")
        void success() throws Exception {
            Student student = new Student();
            student.setId(10L);
            student.setStudentCode("A01");

            Grid grid = new Grid();
            grid.setId(1L);
            grid.setStudent(student);

            when(studentRepository.findById(10L)).thenReturn(Optional.of(student));

            IdpGeneralInfo savedInfo = new IdpGeneralInfo();
            savedInfo.setId(100L);
            when(generalInfoRepository.save(any(IdpGeneralInfo.class))).thenReturn(savedInfo);

            SkillExercise exercise = new SkillExercise();
            exercise.setSkillCodes(List.of("A1"));
            when(objectMapper.writeValueAsString(exercise)).thenReturn("{\"skill\":\"A1\"}");
            when(objectMapper.writeValueAsString(any(Map.class))).thenReturn("{\"student\":{}}");

            service.saveExercisesFromAgent(grid, List.of(exercise), Map.of("info", "test"));

            verify(generalInfoRepository).save(any(IdpGeneralInfo.class));
            verify(exercisesRepository).save(any(IdpExercises.class));
        }

        @Test
        @DisplayName("should handle null skill codes")
        void nullSkillCodes() throws Exception {
            Student student = new Student();
            student.setId(10L);
            student.setStudentCode("A01");

            Grid grid = new Grid();
            grid.setId(1L);
            grid.setStudent(student);

            when(studentRepository.findById(10L)).thenReturn(Optional.of(student));
            when(generalInfoRepository.save(any())).thenReturn(new IdpGeneralInfo());
            when(objectMapper.writeValueAsString(any())).thenReturn("{}");

            SkillExercise exercise = new SkillExercise();
            exercise.setSkillCodes(null);

            service.saveExercisesFromAgent(grid, List.of(exercise), null);

            verify(exercisesRepository).save(any(IdpExercises.class));
        }

        @Test
        @DisplayName("should throw NotFoundException when student not found")
        void studentNotFound() {
            Grid grid = new Grid();
            Student student = new Student();
            student.setId(99L);
            grid.setStudent(student);

            when(studentRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.saveExercisesFromAgent(grid, List.of(), null))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(STUDENT_NOT_FOUND);
        }
    }
}