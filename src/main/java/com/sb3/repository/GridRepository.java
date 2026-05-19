package com.sb3.repository;

import com.sb3.constant.GridStatus;
import com.sb3.entity.grid.Grid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GridRepository extends JpaRepository<Grid, Long> {

    Optional<Grid> findByStudentIdAndGridStatus(Long id, GridStatus gridStatus);

    @Query("SELECT g FROM Grid g JOIN FETCH g.scores s JOIN FETCH s.skill WHERE g.id = :id")
    Optional<Grid> findByIdWithScores(@Param("id") Long id);
}
