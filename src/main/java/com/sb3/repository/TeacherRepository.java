package com.sb3.repository;


import com.sb3.constant.UserRole;
import com.sb3.dto.teacher.TeacherResponse;
import com.sb3.entity.teacher.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<Teacher> findAllByRole(UserRole role, Pageable pageable);
}