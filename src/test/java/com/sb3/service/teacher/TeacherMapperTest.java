package com.sb3.service.teacher;

import com.sb3.constant.TeacherStatus;
import com.sb3.dto.teacher.TeacherRequest;
import com.sb3.dto.teacher.TeacherResponse;
import com.sb3.dto.teacher.TeacherStatusRequest;
import com.sb3.entity.teacher.Teacher;
import com.sb3.mapper.TeacherMapper;
import com.sb3.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.sb3.constant.UserRole.TEACHER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import com.sb3.mapper.StudentMapper;
import org.junit.jupiter.api.DisplayName;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
@DisplayName("TeacherMapper")
class TeacherMapperTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    private TeacherMapper teacherMapper;
    private Teacher teacher;
    private TeacherRequest request;

    @BeforeEach
    void setUp() {
        teacherMapper = new TeacherMapper(studentRepository, studentMapper);

        teacher = Teacher.builder()
                .id(1L)
                .lastName("Иванов")
                .firstName("Иван")
                .patronymic("Иванович")
                .email("ivan@example.com")
                .phone("+7 900 123-45-67")
                .specialization("Психолог")
                .status(TeacherStatus.ACTIVE)
                .role(TEACHER)
                .passwordUpdatedAt(LocalDateTime.now())
                .students(new ArrayList<>())
                .build();

        request = TeacherRequest.builder()
                .lastName("Петров")
                .firstName("Петр")
                .patronymic("Петрович")
                .email("petr@example.com")
                .phone("+7 900 765-43-21")
                .specialization("Дефектолог")
                .status(TeacherStatusRequest.ACTIVE)
                .build();

        // Настройка lenient моков для методов, которые могут не вызываться
        lenient().when(studentRepository.findAllById(any())).thenReturn(new ArrayList<>());
        lenient().when(studentMapper.toShortResponse(any())).thenReturn(null);
    }

    @Test
    @DisplayName("Should map TeacherRequest to Teacher entity")
    void shouldMapToEntity() {
        // when
        Teacher result = teacherMapper.toEntity(request);

        // then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getLastName()).isEqualTo("Петров"),
                () -> assertThat(result.getFirstName()).isEqualTo("Петр"),
                () -> assertThat(result.getPatronymic()).isEqualTo("Петрович"),
                () -> assertThat(result.getEmail()).isEqualTo("petr@example.com"),
                () -> assertThat(result.getPhone()).isEqualTo("+7 900 765-43-21"),
                () -> assertThat(result.getSpecialization()).isEqualTo("Дефектолог"),
                () -> assertThat(result.getStatus()).isEqualTo(TeacherStatus.ACTIVE)
        );
    }

    @Test
    @DisplayName("Should map Teacher entity to TeacherResponse")
    void shouldMapToResponse() {
        // when
        TeacherResponse result = teacherMapper.toResponse(teacher);

        // then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getFullName()).isEqualTo("Иванов Иван Иванович"),
                () -> assertThat(result.getEmail()).isEqualTo("ivan@example.com"),
                () -> assertThat(result.getSpecialization()).isEqualTo("Психолог"),
                () -> assertThat(result.getStatus()).isEqualTo("ACTIVE")
        );
    }

    @Test
    @DisplayName("Should map Teacher to TeacherBriefDto")
    void shouldMapToBriefDto() {
        // when
        var result = teacherMapper.toBriefDto(teacher);

        // then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getFullName()).isEqualTo("Иванов Иван Иванович"),
                () -> assertThat(result.getSpecialization()).isEqualTo("Психолог")
        );
    }

    @Test
    @DisplayName("Should update existing Teacher entity from TeacherRequest")
    void shouldUpdateEntity() {
        // given
        Teacher existingTeacher = Teacher.builder()
                .id(1L)
                .lastName("Иванов")
                .firstName("Иван")
                .patronymic("Иванович")
                .email("old@example.com")
                .phone("+7 900 111-11-11")
                .specialization("Психолог")
                .status(TeacherStatus.ACTIVE)
                .role(TEACHER)
                .students(new ArrayList<>())
                .build();

        // when
        teacherMapper.updateEntity(existingTeacher, request);

        // then
        assertAll(
                () -> assertThat(existingTeacher.getLastName()).isEqualTo("Петров"),
                () -> assertThat(existingTeacher.getFirstName()).isEqualTo("Петр"),
                () -> assertThat(existingTeacher.getPatronymic()).isEqualTo("Петрович"),
                () -> assertThat(existingTeacher.getEmail()).isEqualTo("petr@example.com"),
                () -> assertThat(existingTeacher.getPhone()).isEqualTo("+7 900 765-43-21"),
                () -> assertThat(existingTeacher.getSpecialization()).isEqualTo("Дефектолог"),
                () -> assertThat(existingTeacher.getStatus()).isEqualTo(TeacherStatus.ACTIVE)
        );
    }

    @Test
    @DisplayName("Should handle null patronymic correctly")
    void shouldHandleNullPatronymic() {
        // given
        Teacher teacherWithoutPatronymic = Teacher.builder()
                .id(2L)
                .lastName("Сидоров")
                .firstName("Сидор")
                .patronymic(null)
                .email("sidor@example.com")
                .specialization("Логопед")
                .status(TeacherStatus.ACTIVE)
                .role(TEACHER)
                .students(new ArrayList<>())
                .build();

        // when
        TeacherResponse result = teacherMapper.toResponse(teacherWithoutPatronymic);

        // then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getFullName()).isEqualTo("Сидоров Сидор")
        );
    }

    @Test
    @DisplayName("Should handle null status by defaulting to ACTIVE")
    void shouldHandleNullStatus() {
        // given
        TeacherRequest requestWithNullStatus = TeacherRequest.builder()
                .lastName("Тестов")
                .firstName("Тест")
                .email("test@example.com")
                .specialization("Тест")
                .status(null)
                .build();

        // when
        Teacher result = teacherMapper.toEntity(requestWithNullStatus);

        // then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getStatus()).isEqualTo(TeacherStatus.ACTIVE)
        );
    }

    @Test
    @DisplayName("Should handle null request in toEntity")
    void shouldHandleNullRequestInToEntity() {
        // when
        Teacher result = teacherMapper.toEntity(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle null teacher in toResponse")
    void shouldHandleNullTeacherInToResponse() {
        // when
        TeacherResponse result = teacherMapper.toResponse(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle null teacher in toBriefDto")
    void shouldHandleNullTeacherInToBriefDto() {
        // when
        var result = teacherMapper.toBriefDto(null);

        // then
        assertThat(result).isNull();
    }
}