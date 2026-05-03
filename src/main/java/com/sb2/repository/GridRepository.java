package com.sb2.repository;

import com.sb2.constant.GridStatus;
import com.sb2.entity.Grid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GridRepository extends JpaRepository<Grid, Long> {
    Grid findByGridId(Long gridId);

    Optional<Grid> findByStudentIdAndGridStatus(Long id, GridStatus gridStatus);
}
