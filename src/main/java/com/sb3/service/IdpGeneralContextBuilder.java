package com.sb3.service;

import com.sb3.dto.idp.context.*;
import com.sb3.entity.student.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IdpGeneralContextBuilder {

    public IdpGeneralContextDto build(Student student) {

        return IdpGeneralContextDto.builder()

                .environment(buildEnvironment(student))

                .recommendations(buildRecommendations(student))

                .preferredReinforcers(buildPreferredReinforcers(student))

                .reinforcementSystems(buildReinforcementSystems(student))

                .errorCorrectionProcedure(buildErrorCorrection(student))

                .build();
    }

    private EnvironmentDto buildEnvironment(Student student) {

        LearningSkills ls = student.getLearningSkills();

        return EnvironmentDto.builder()
                .physicalArrangement(extractSchoolInfo(student))
                .specialEquipment(student.getAdditionalNotes())
                .sessionStructure(buildSessionStructure(ls))
                .build();
    }

    private SessionStructureDto buildSessionStructure(LearningSkills ls) {

        if (ls == null || ls.getAttentionSpanMinutes() == null) {
            return null;
        }

        return SessionStructureDto.builder()
                .typicalWorkDuration(ls.getAttentionSpanMinutes() + " минут")
                .typicalBreakDuration("3-5 минут")
                .build();
    }

    private RecommendationsDto buildRecommendations(Student student) {

        return RecommendationsDto.builder()
                .generalMethods(student.getStrengths())
                .behaviorManagement(student.getChallenges())
                .reviewAndReinforcement(student.getAchievements3months())
                .additionalRecommendations(student.getAdditionalNotes())
                .build();
    }

    private String buildPreferredReinforcers(Student student) {

        Motivation motivation = student.getMotivation();

        if (motivation == null || motivation.getPreferred() == null) {
            return null;
        }

        return motivation.getPreferred().toString();
    }

    private List<ReinforcementSystemDto> buildReinforcementSystems(Student student) {

        return List.of(
                ReinforcementSystemDto.builder()
                        .name("Базовая система подкрепления")
                        .type("social")
                        .description(student.getGoals3months())
                        .build()
        );
    }

    private ErrorCorrectionProcedureDto buildErrorCorrection(Student student) {

        LearningSkills ls = student.getLearningSkills();

        if (ls == null) {
            return null;
        }

        return ErrorCorrectionProcedureDto.builder()
                .type(ls.getAcquisitionSpeed())
                .description(ls.getRetention())
                .build();
    }

    private String extractSchoolInfo(Student student) {

        SchoolInfo schoolInfo = student.getSchoolInfo();

        if (schoolInfo == null) {
            return null;
        }

        return String.join(
                ", ",
                safe(schoolInfo.getType()),
                safe(schoolInfo.getSpecialization()),
                safe(schoolInfo.getCurrentProgram())
        );
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
