package com.sb3.entity.grid;

import com.sb3.constant.GridStatus;
import com.sb3.entity.skill.SkillScore;
import com.sb3.entity.student.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "grids")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Grid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private GridStatus gridStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private Long tutorId;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "grid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SkillScore> scores = new ArrayList<>();
}
