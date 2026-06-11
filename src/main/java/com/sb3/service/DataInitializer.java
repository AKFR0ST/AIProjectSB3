package com.sb3.service;

import com.sb3.constant.TeacherStatus;
import com.sb3.constant.UserRole;
import com.sb3.dto.skill.SkillImportDto;
import com.sb3.entity.category.Category;
import com.sb3.entity.skill.Skill;
import com.sb3.entity.teacher.Teacher;
import com.sb3.repository.CategoryRepository;
import com.sb3.repository.SkillRepository;
import com.sb3.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final SkillRepository skillRepository;
    private final CategoryRepository categoryRepository;
    private final TeacherRepository teacherRepository;  // ← добавить
    private final PasswordEncoder passwordEncoder;      // ← добавить

    @Override
    public void run(String... args) throws Exception {

        // 1. Инициализация навыков (ABLLS)
        initSkills();

        // 2. Инициализация преподавателей
        initTeachers();
    }

    private void initSkills() throws Exception {
        if (skillRepository.count() > 0) {
            return; // уже есть данные
        }

        Category category = categoryRepository.findByCode("A")
                .orElseGet(() -> {
                    Category c = new Category();
                    c.setCode("A");
                    c.setName("Cooperation");
                    return categoryRepository.save(c);
                });

        InputStream is = new ClassPathResource("ablls/skills.json").getInputStream();

        List<SkillImportDto> dtos = objectMapper.readValue(
                is,
                new TypeReference<List<SkillImportDto>>() {}
        );

        List<Skill> skills = dtos.stream().map(dto -> {
            Skill skill = new Skill();
            skill.setCode(dto.getCode());
            skill.setMaxScore(dto.getMaxScore());
            skill.setTask(dto.getTask());
            skill.setTaskDesc(dto.getTaskDesc());
            skill.setQuestion(dto.getQuestion());
            skill.setExample(dto.getExample());
            skill.setCriteria(dto.getCriteria());
            skill.setComment(dto.getComment());
            skill.setCategory(category);
            return skill;
        }).toList();

        skillRepository.saveAll(skills);
    }

    private void initTeachers() {
        if (teacherRepository.count() > 0) {
            return; // уже есть преподаватели
        }

        log.info("Creating initial teachers...");

        // ADMIN
        Teacher admin = Teacher.builder()
                .lastName("Администраторов")
                .firstName("Админ")
                .patronymic("Админович")
                .email("admin@system.local")
                .phone("+7 900 000-00-00")
                .specialization("System Administrator")
                .status(TeacherStatus.ACTIVE)
                .password(passwordEncoder.encode("admin"))
                .role(UserRole.ADMIN)
                .build();

        // AI_AGENT
        Teacher aiAgent = Teacher.builder()
                .lastName("AI")
                .firstName("Agent")
                .email("agent@system.local")
                .specialization("AI Assistant")
                .status(TeacherStatus.ACTIVE)
                .password(passwordEncoder.encode("agent-secret-password"))
                .role(UserRole.AI_AGENT)
                .build();

        Teacher teacher1 = Teacher.builder()
                .lastName("Морозова")
                .firstName("Анна")
                .patronymic("Игоревна")
                .email("anna.morozova@example.com")
                .phone("+7 900 101-20-30")
                .specialization("Логопед-дефектолог")
                .status(TeacherStatus.ACTIVE)
                .password(passwordEncoder.encode("password123"))
                .build();

        Teacher teacher2 = Teacher.builder()
                .lastName("Соколов")
                .firstName("Игорь")
                .patronymic("Александрович")
                .email("igor.sokolov@example.com")
                .phone("+7 900 101-20-31")
                .specialization("Поведенческий аналитик")
                .status(TeacherStatus.ACTIVE)
                .password(passwordEncoder.encode("password123"))
                .build();

        Teacher teacher3 = Teacher.builder()
                .lastName("Кузнецова")
                .firstName("Мария")
                .patronymic("Владимировна")
                .email("maria.kuznetsova@example.com")
                .phone("+7 900 101-20-32")
                .specialization("Тьютор")
                .status(TeacherStatus.INACTIVE)
                .password(passwordEncoder.encode("password123"))
                .build();

        teacherRepository.saveAll(List.of(teacher1, teacher2, teacher3));

        log.info("Created {} teachers", teacherRepository.count());
    }
}
