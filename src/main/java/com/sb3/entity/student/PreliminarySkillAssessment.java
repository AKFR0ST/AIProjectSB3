package com.sb3.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "preliminary_skill_assessment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreliminarySkillAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cooperation_with_adults", columnDefinition = "text")
    private String cooperationWithAdults;

    @Column(columnDefinition = "text")
    private String requests;

    @Column(name = "motor_imitation", columnDefinition = "text")
    private String motorImitation;

    @Column(name = "speech_skills", columnDefinition = "text")
    private String speechSkills;

    @Column(name = "vocal_imitation", columnDefinition = "text")
    private String vocalImitation;

    @Column(name = "visual_perception", columnDefinition = "text")
    private String visualPerception;

    @Column(name = "following_instructions", columnDefinition = "text")
    private String followingInstructions;

    @Column(name = "conversation_skills", columnDefinition = "text")
    private String conversationSkills;

    @Column(name = "letters_and_numbers", columnDefinition = "text")
    private String lettersAndNumbers;

    @Column(name = "social_interaction", columnDefinition = "text")
    private String socialInteraction;

    @Column(name = "play_skills", columnDefinition = "text")
    private String playSkills;
}