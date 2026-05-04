package com.sb2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // A1, A2

    @Column(nullable = false)
    private Integer maxScore;

    @Column(nullable = false)
    private String task;

    @Column(length = 2000)
    private String taskDesc;

    @Column(length = 2000)
    private String question;

    @Column(length = 2000)
    private String example;

    @Column(length = 2000)
    private String criteria;

    @Column(length = 2000)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
