package com.sb3.service.teacher;

import com.sb3.constant.TeacherStatus;
import com.sb3.dto.teacher.TeacherRequest;
import com.sb3.dto.teacher.TeacherResponse;
import com.sb3.dto.teacher.TeacherStatusRequest;
import com.sb3.entity.teacher.Teacher;
import com.sb3.mapper.TeacherMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    @BeforeEach
    void setUp() {
        teacherMapper = new TeacherMapper();
    }

    @Test
    void shouldMapToEntity() {
        // given
        TeacherRequest request = TeacherRequest.builder()
                .lastName("Морозова")
                .firstName("Анна")
                .patronymic("Игоревна")
                .email("anna.morozova@example.com")
                .phone("+7 900 101-20-30")
                .specialization("Логопед-дефектолог")
                .status(TeacherStatusRequest.ACTIVE)
                .build();

        // when
        Teacher teacher = teacherMapper.toEntity(request);

        // then
        assertAll(
                () -> assertThat(teacher).isNotNull(),
                () -> assertThat(teacher.getLastName()).isEqualTo("Морозова"),
                () -> assertThat(teacher.getFirstName()).isEqualTo("Анна"),
                () -> assertThat(teacher.getPatronymic()).isEqualTo("Игоревна"),
                () -> assertThat(teacher.getEmail()).isEqualTo("anna.morozova@example.com"),
                () -> assertThat(teacher.getPhone()).isEqualTo("+7 900 101-20-30"),
                () -> assertThat(teacher.getSpecialization()).isEqualTo("Логопед-дефектолог"),
                () -> assertThat(teacher.getStatus()).isEqualTo(TeacherStatus.ACTIVE)
        );
    }

    @Test
    void shouldMapToResponse() {
        // given
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Соколов")
                .firstName("Игорь")
                .patronymic("Александрович")
                .email("igor.sokolov@example.com")
                .phone("+7 900 101-20-31")
                .specialization("Поведенческий аналитик")
                .status(TeacherStatus.ACTIVE)
                .passwordUpdatedAt(LocalDateTime.of(2026, 5, 12, 15, 10))
                .createdAt(LocalDateTime.of(2026, 5, 1, 10, 0))
                .updatedAt(LocalDateTime.of(2026, 5, 12, 15, 10))
                .build();

        // when
        TeacherResponse response = teacherMapper.toResponse(teacher);

        // then
        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.getId()).isEqualTo(1L),
                () -> assertThat(response.getFullName()).isEqualTo("Соколов Игорь Александрович"),
                () -> assertThat(response.getEmail()).isEqualTo("igor.sokolov@example.com"),
                () -> assertThat(response.getSpecialization()).isEqualTo("Поведенческий аналитик"),
                () -> assertThat(response.getStatus()).isEqualTo("ACTIVE")
        );
    }

    @Test
    void shouldMapToBriefDto() {
        // given
        Teacher teacher = Teacher.builder()
                .id(2L)
                .lastName("Кузнецова")
                .firstName("Мария")
                .specialization("Тьютор")
                .status(TeacherStatus.ACTIVE)
                .build();

        // when
        var briefDto = teacherMapper.toBriefDto(teacher);

        // then
        assertAll(
                () -> assertThat(briefDto).isNotNull(),
                () -> assertThat(briefDto.getId()).isEqualTo(2L),
                () -> assertThat(briefDto.getFullName()).isEqualTo("Кузнецова Мария"),
                () -> assertThat(briefDto.getSpecialization()).isEqualTo("Тьютор")
        );
    }

    @Test
    void shouldUpdateEntity() {
        // given
        Teacher teacher = Teacher.builder()
                .lastName("Иванов")
                .firstName("Петр")
                .specialization("Психолог")
                .status(TeacherStatus.ACTIVE)
                .build();

        TeacherRequest request = TeacherRequest.builder()
                .lastName("Петров")
                .specialization("Дефектолог")
                .status(TeacherStatusRequest.INACTIVE)
                .build();

        // when
        teacherMapper.updateEntity(teacher, request);

        // then
        assertAll(
                () -> assertThat(teacher.getLastName()).isEqualTo("Петров"),
                () -> assertThat(teacher.getFirstName()).isEqualTo("Петр"), // не изменилось
                () -> assertThat(teacher.getSpecialization()).isEqualTo("Дефектолог"),
                () -> assertThat(teacher.getStatus()).isEqualTo(TeacherStatus.INACTIVE)
        );
    }

    @Test
    void shouldHandleNullPatronymic() {
        // given
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Сидоров")
                .firstName("Алексей")
                .patronymic(null)
                .status(TeacherStatus.ACTIVE)  // ← добавили статус
                .build();

        // when
        TeacherResponse response = teacherMapper.toResponse(teacher);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getFullName()).isEqualTo("Сидоров Алексей");
        assertThat(response.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void shouldHandleNullTeacher() {
        // when
        TeacherResponse response = teacherMapper.toResponse(null);

        // then
        assertThat(response).isNull();
    }

    @Test
    void shouldHandleNullStatus() {
        // given
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Петров")
                .firstName("Иван")
                .status(null)  // статус null
                .build();

        // when
        TeacherResponse response = teacherMapper.toResponse(teacher);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isNull();  // должен быть null, не падать
    }
}