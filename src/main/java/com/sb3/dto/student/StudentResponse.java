package com.sb3.dto.student;

import com.sb3.entity.student.*;
import lombok.Data;

import java.util.List;

@Data
public class StudentResponse {
    private Long id;
    private Long studentId;
    private String studentCode;
    private PersonalInfo personalInfo;
    private MedicalInfo medicalInfo;
    private String videoLinks;
    private String mainConcern;
    private AbaHistory abaHistory;
    private String strengths;
    private String challenges;
    private String achievements3months;
    private String achievements1year;
    private String goals3months;
    private String goals1year;
    private Communication communication;
    private SocialAndPlay socialAndPlay;
    private LearningSkills learningSkills;
    private Behavior behavior;
    private List<ProblemBehavior> problemBehaviors;
    private List<SelfStimulatoryBehavior> selfStimulatoryBehaviors;
    private Motivation motivation;
    private DailyRoutine dailyRoutine;
    private SchoolInfo schoolInfo;
    private PreliminarySkillAssessment preliminarySkillAssessment;
    private String additionalNotes;
}