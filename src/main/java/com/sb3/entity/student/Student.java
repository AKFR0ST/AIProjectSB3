package com.sb3.entity.student;

import com.sb3.entity.grid.Grid;
import com.sb3.entity.idp.IdpGeneralInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_code")
    private String studentCode;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "personal_info_id")
    private PersonalInfo personalInfo;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "medical_info_id")
    private MedicalInfo medicalInfo;

    @Column(name = "video_links", columnDefinition = "text")
    private String videoLinks;

    @Column(name = "main_concern", columnDefinition = "text")
    private String mainConcern;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "aba_history_id")
    private AbaHistory abaHistory;

    @Column(columnDefinition = "text")
    private String strengths;

    @Column(columnDefinition = "text")
    private String challenges;

    @Column(name = "achievements_3months", columnDefinition = "text")
    private String achievements3months;

    @Column(name = "achievements_1year", columnDefinition = "text")
    private String achievements1year;

    @Column(name = "goals_3months", columnDefinition = "text")
    private String goals3months;

    @Column(name = "goals_1year", columnDefinition = "text")
    private String goals1year;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "communication_id")
    private Communication communication;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "social_and_play_id")
    private SocialAndPlay socialAndPlay;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "learning_skills_id")
    private LearningSkills learningSkills;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "behavior_id")
    private Behavior behavior;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "assessment_id")
    private List<ProblemBehavior> problemBehaviors = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "assessment_id")
    private List<SelfStimulatoryBehavior> selfStimulatoryBehaviors = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "motivation_id")
    private Motivation motivation;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "daily_routine_id")
    private DailyRoutine dailyRoutine;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "school_info_id")
    private SchoolInfo schoolInfo;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "preliminary_skill_assessment_id")
    private PreliminarySkillAssessment preliminarySkillAssessment;

    @Column(name = "additional_notes", columnDefinition = "text")
    private String additionalNotes;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IdpGeneralInfo> idpGeneralInfoList = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grid> grids = new ArrayList<>();
}
