package com.sb3.service;

import com.sb3.constant.GridStatus;
import com.sb3.dto.exercise.ExercisesRequestDto;
import com.sb3.dto.exercise.ExercisesResponseDto;
import com.sb3.dto.idp.GenerateGeneralInfoResponseDto;
import com.sb3.dto.skill.SkillExerciseDto;
import com.sb3.entity.grid.Grid;
import com.sb3.entity.grid.SkillScore;
import com.sb3.entity.skill.Skill;
import com.sb3.entity.skill.SkillExercise;
import com.sb3.entity.student.Student;
import com.sb3.entity.task.Task;
import com.sb3.entity.task.TaskStatus;
import com.sb3.exception.TaskNotFoundException;
import com.sb3.mapper.SkillExerciseMapper;
import com.sb3.repository.GridRepository;
import com.sb3.repository.TaskRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sb3.dto.idp.GenerateGeneralInfoRequestDto;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskService")
class TaskServiceTest {

    @Mock private TaskRepository repository;
    @Mock private GridRepository gridRepository;
    @Mock private GridService gridService;
    @Mock private ObjectMapper objectMapper;
    @Mock private IdpService idpService;
    @Mock private RestClient restClient;
    @Mock private SkillExerciseMapper skillExerciseMapper;

    @InjectMocks
    private TaskService service;

    @Nested
    @DisplayName("createTask")
    class CreateTask {

        @Test
        @DisplayName("returns saved task with CREATED status for a valid gridId")
        void createTask_success() {
            long gridId = 42L;
            Task saved = new Task();
            saved.setId(UUID.randomUUID());
            saved.setGridId(gridId);
            saved.setStatus(TaskStatus.CREATED);
            when(repository.save(any(Task.class))).thenReturn(saved);

            Task result = service.createTask(gridId);

            assertThat(result.getGridId()).isEqualTo(gridId);
            assertThat(result.getStatus()).isEqualTo(TaskStatus.CREATED);
            verify(gridService).setStatus(gridId, GridStatus.PROCESSING);
            verify(repository).save(any(Task.class));
        }

        @Test
        @DisplayName("sets PROCESSING status on the grid before saving the task")
        void createTask_setsGridProcessing() {
            when(repository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

            service.createTask(7L);

            verify(gridService).setStatus(7L, GridStatus.PROCESSING);
        }

        @Test
        @DisplayName("throws IllegalArgumentException when gridId is null")
        void createTask_nullGridId_throws() {
            assertThatThrownBy(() -> service.createTask(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("gridId must not be empty");

            verifyNoInteractions(repository, gridService);
        }
    }

    @Nested
    @DisplayName("getTask")
    class GetTask {

        @Test
        @DisplayName("returns task when it exists")
        void getTask_found() {
            UUID id = UUID.randomUUID();
            Task task = new Task();
            task.setId(id);
            task.setGridId(1L);
            task.setStatus(TaskStatus.DONE);
            when(repository.findById(id)).thenReturn(Optional.of(task));

            Task result = service.getTask(id);

            assertThat(result.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("throws TaskNotFoundException when task does not exist")
        void getTask_notFound_throws() {
            UUID id = UUID.randomUUID();
            when(repository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getTask(id))
                    .isInstanceOf(TaskNotFoundException.class);
        }
    }


    @Nested
    @DisplayName("processTask")
    @MockitoSettings(strictness = Strictness.LENIENT)
    class ProcessTask {

        private UUID taskId;
        private Task task;
        private Grid grid;

        private RestClient.RequestBodyUriSpec uriSpec;
        private RestClient.RequestBodySpec bodySpec;
        private RestClient.ResponseSpec responseSpec;

        @BeforeEach
        void setUp() {
            taskId = UUID.randomUUID();
            task = new Task();
            task.setId(taskId);
            task.setGridId(1L);
            task.setStatus(TaskStatus.CREATED);

            Student student = new Student();
            student.setId(10L);

            Skill skill = new Skill();
            skill.setCode("A-1");

            SkillScore score = new SkillScore();
            score.setSkill(skill);
            score.setScore(3);

            grid = new Grid();
            grid.setId(1L);
            grid.setStudent(student);
            grid.setScores(new ArrayList<>(List.of(score)));

            uriSpec = mock(RestClient.RequestBodyUriSpec.class);
            bodySpec = mock(RestClient.RequestBodySpec.class);
            responseSpec = mock(RestClient.ResponseSpec.class);

            when(restClient.post()).thenReturn(uriSpec);
            when(uriSpec.uri(anyString())).thenReturn(bodySpec);
            when(bodySpec.contentType(any())).thenReturn(bodySpec);
            when(bodySpec.body(any(ExercisesRequestDto.class))).thenReturn(bodySpec);
            when(bodySpec.body(any(GenerateGeneralInfoRequestDto.class))).thenReturn(bodySpec);
            when(bodySpec.retrieve()).thenReturn(responseSpec);
        }

        @Test
        @DisplayName("marks task DONE and grid DONE on successful processing")
        void processTask_success() throws Exception {
            when(repository.findById(taskId)).thenReturn(Optional.of(task));
            when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(gridRepository.findByIdWithScores(1L)).thenReturn(Optional.of(grid));

            ExercisesResponseDto exercisesResponse = new ExercisesResponseDto();
            exercisesResponse.setStatus("success");
            exercisesResponse.setDraft(List.of(new SkillExerciseDto()));

            GenerateGeneralInfoResponseDto generalInfoResponse = new GenerateGeneralInfoResponseDto();
            generalInfoResponse.setStatus("success");
            generalInfoResponse.setDraft(new Object());

            when(responseSpec.body(String.class))
                    .thenReturn("{\"status\":\"success\",\"draft\":[]}")
                    .thenReturn("{\"status\":\"success\",\"draft\":{}}");

            when(objectMapper.readValue("{\"status\":\"success\",\"draft\":[]}", ExercisesResponseDto.class))
                    .thenReturn(exercisesResponse);
            when(objectMapper.readValue("{\"status\":\"success\",\"draft\":{}}", GenerateGeneralInfoResponseDto.class))
                    .thenReturn(generalInfoResponse);
            when(objectMapper.writeValueAsString(any())).thenReturn("{}");
            when(skillExerciseMapper.toEntityList(any())).thenReturn(List.of(new SkillExercise()));

            service.processTask(taskId);

            assertThat(task.getStatus()).isEqualTo(TaskStatus.DONE);
            verify(gridService).setStatus(1L, GridStatus.DONE);
            verify(idpService).saveExercisesFromAgent(eq(grid), any(), any());
        }

        @Test
        @DisplayName("marks task FAILED and grid DRAFT when agent returns error status")
        void processTask_agentErrorStatus_marksFailedAndResetGrid() throws Exception {
            when(repository.findById(taskId)).thenReturn(Optional.of(task));
            when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(gridRepository.findByIdWithScores(1L)).thenReturn(Optional.of(grid));

            ExercisesResponseDto errorResponse = new ExercisesResponseDto();
            errorResponse.setStatus("error");
            errorResponse.setMessage("upstream failure");

            when(responseSpec.body(String.class)).thenReturn("{\"status\":\"error\",\"message\":\"upstream failure\"}");
            when(objectMapper.readValue(anyString(), eq(ExercisesResponseDto.class))).thenReturn(errorResponse);
            when(objectMapper.writeValueAsString(any())).thenReturn("{}");

            service.processTask(taskId);

            assertThat(task.getStatus()).isEqualTo(TaskStatus.FAILED);
            assertThat(task.getError()).contains("upstream failure");
            verify(gridService).setStatus(1L, GridStatus.DRAFT);
            verifyNoInteractions(idpService);
        }

        @Test
        @DisplayName("marks task FAILED when grid is not found")
        void processTask_gridNotFound_marksTaskFailed() {
            when(repository.findById(taskId)).thenReturn(Optional.of(task));
            when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(gridRepository.findByIdWithScores(1L)).thenReturn(Optional.empty());

            service.processTask(taskId);

            assertThat(task.getStatus()).isEqualTo(TaskStatus.FAILED);
            assertThat(task.getError()).contains("Grid not found");
            verify(gridService).setStatus(1L, GridStatus.DRAFT);
        }

        @Test
        @DisplayName("always saves task in finally block, even on failure")
        void processTask_alwaysSavesInFinally() {
            when(repository.findById(taskId)).thenReturn(Optional.of(task));
            when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(gridRepository.findByIdWithScores(1L)).thenReturn(Optional.empty());

            service.processTask(taskId);

            verify(repository, atLeast(2)).save(any());
        }

        @Test
        @DisplayName("sets updatedAt on task after processing")
        void processTask_setsUpdatedAt() {
            when(repository.findById(taskId)).thenReturn(Optional.of(task));
            when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(gridRepository.findByIdWithScores(1L)).thenReturn(Optional.empty());

            LocalDateTime before = LocalDateTime.now().minusSeconds(1);
            service.processTask(taskId);

            assertThat(task.getUpdatedAt()).isAfterOrEqualTo(before);
        }
    }
}