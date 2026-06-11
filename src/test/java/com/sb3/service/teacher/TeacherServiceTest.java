package com.sb3.service.teacher;

import com.sb3.constant.TeacherStatus;
import com.sb3.constant.UserRole;
import com.sb3.dto.teacher.TeacherRequest;
import com.sb3.dto.teacher.TeacherResponse;
import com.sb3.dto.teacher.TeacherStatusRequest;
import com.sb3.entity.teacher.Teacher;
import com.sb3.exception.NotFoundException;
import com.sb3.mapper.TeacherMapper;
import com.sb3.repository.TeacherRepository;
import com.sb3.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;
    private TeacherRequest request;
    private TeacherResponse response;

    @BeforeEach
    void setUp() {
        teacher = Teacher.builder()
                .id(1L)
                .lastName("Морозова")
                .firstName("Анна")
                .patronymic("Игоревна")
                .email("anna.morozova@example.com")
                .phone("+7 900 101-20-30")
                .specialization("Логопед-дефектолог")
                .status(TeacherStatus.ACTIVE)
                .role(UserRole.TEACHER)
                .passwordUpdatedAt(LocalDateTime.now())
                .build();

        request = TeacherRequest.builder()
                .lastName("Морозова")
                .firstName("Анна")
                .email("anna.morozova@example.com")
                .specialization("Логопед-дефектолог")
                .status(TeacherStatusRequest.ACTIVE)
                .build();

        response = TeacherResponse.builder()
                .id(1L)
                .lastName("Морозова")
                .firstName("Анна")
                .fullName("Морозова Анна Игоревна")
                .email("anna.morozova@example.com")
                .specialization("Логопед-дефектолог")
                .status("ACTIVE")
                .build();
    }

    @Test
    void shouldCreateTeacher() {
        // given
        when(teacherMapper.toEntity(request)).thenReturn(teacher);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);
        when(teacherMapper.toResponse(teacher)).thenReturn(response);

        // when
        TeacherResponse result = teacherService.createTeacher(request);

        // then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getFullName()).isEqualTo("Морозова Анна Игоревна")
        );

        verify(teacherRepository, times(1)).save(any(Teacher.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
    }

    @Test
    void shouldGetAllTeachersWithPagination() {
        // given
        Pageable pageable = PageRequest.of(0, 20);
        Page<Teacher> teacherPage = new PageImpl<>(List.of(teacher), pageable, 1);

        when(teacherRepository.findAll(pageable)).thenReturn(teacherPage);
        when(teacherMapper.toResponse(any(Teacher.class))).thenReturn(response);

        // when
        Page<TeacherResponse> result = teacherService.getAllTeachers(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
        assertThat(result.getTotalElements()).isEqualTo(1);

        verify(teacherRepository, times(1)).findAll(pageable);
    }

    @Test
    void shouldGetTeacherById() {
        // given
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherMapper.toResponse(teacher)).thenReturn(response);

        // when
        TeacherResponse result = teacherService.getTeacherById(1L);

        // then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getEmail()).isEqualTo("anna.morozova@example.com")
        );
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTeacherNotFound() {
        // given
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> teacherService.getTeacherById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Teacher not found with id: 99");
    }

    @Test
    void shouldUpdateTeacher() {
        // given
        TeacherRequest updateRequest = TeacherRequest.builder()
                .lastName("Соколова")
                .specialization("Специальный психолог")
                .build();

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        doNothing().when(teacherMapper).updateEntity(any(Teacher.class), any(TeacherRequest.class));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);
        when(teacherMapper.toResponse(teacher)).thenReturn(response);

        // when
        TeacherResponse result = teacherService.updateTeacher(1L, updateRequest);

        // then
        assertThat(result).isNotNull();
        verify(teacherMapper, times(1)).updateEntity(teacher, updateRequest);
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void shouldDeleteTeacher() {
        // given
        when(teacherRepository.existsById(1L)).thenReturn(true);
        doNothing().when(teacherRepository).deleteById(1L);

        // when
        teacherService.deleteTeacher(1L);

        // then
        verify(teacherRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentTeacher() {
        // given
        when(teacherRepository.existsById(99L)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> teacherService.deleteTeacher(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Teacher not found with id: 99");

        verify(teacherRepository, never()).deleteById(any());
    }

    @Test
    void shouldUpdateTeacherStatus() {
        // given
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);
        when(teacherMapper.toResponse(teacher)).thenReturn(response);

        // when
        TeacherResponse result = teacherService.updateTeacherStatus(1L, TeacherStatus.INACTIVE);

        // then
        assertThat(result).isNotNull();
        assertThat(teacher.getStatus()).isEqualTo(TeacherStatus.INACTIVE);
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void shouldUpdateTeacherRole() {
        // given
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);
        when(teacherMapper.toResponse(teacher)).thenReturn(response);

        // when
        TeacherResponse result = teacherService.updateTeacherRole(1L, UserRole.ADMIN);

        // then
        assertThat(result).isNotNull();
        assertThat(teacher.getRole()).isEqualTo(UserRole.ADMIN);
        verify(teacherRepository, times(1)).save(teacher);
    }

}