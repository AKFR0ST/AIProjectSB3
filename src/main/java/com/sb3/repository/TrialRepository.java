package com.sb3.repository;

import com.sb3.entity.idp.Trial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface TrialRepository extends JpaRepository<Trial, UUID> {

    List<Trial> findByExerciseIdAndTrialDateBetweenOrderByCreatedAtDesc(
            Long exerciseId, Instant from, Instant to);

    List<Trial> findByExerciseIdOrderByCreatedAtDesc(Long exerciseId);
}
