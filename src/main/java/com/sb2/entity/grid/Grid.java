package com.sb2.entity.grid;

import com.sb2.constant.GridStatus;
import com.sb2.entity.skill.SkillScore;
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

    private Long studentId;

    private Long tutorId;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "grid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SkillScore> scores = new ArrayList<>();
}
