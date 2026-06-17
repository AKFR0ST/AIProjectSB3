package com.sb3.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "communication")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Communication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "how_requests_desired")
    private String howRequestsDesired;

    @Column(name = "request_help")
    private String requestHelp;

    @Column(name = "report_pain")
    private String reportPain;

    @Column(name = "request_attention")
    private String requestAttention;

    @Column(name = "can_wait_calmly")
    private String canWaitCalmly;

    @Column(name = "eye_contact")
    private Boolean eyeContact;

    @Column(name = "follows_adult_pointing")
    private Boolean followsAdultPointing;

    @Column(name = "understands_adult_gaze_direction")
    private Boolean understandsAdultGazeDirection;

    @Column(name = "responds_to_name")
    private Boolean respondsToName;

    @Column(name = "name_used")
    private String nameUsed;

    @Column(name = "understands_other_people_speech")
    private String understandsOtherPeopleSpeech;

    @Column(name = "understands_adult_instructions")
    private Boolean understandsAdultInstructions;

    @Column(name = "can_repeat_sound_after_adult")
    private Boolean canRepeatSoundAfterAdult;

    @Column(name = "active_vocabulary")
    private Boolean activeVocabulary;

    @Column(name = "label_count")
    private Integer labelCount;

    @Column(name = "continues_songs_after_adult")
    private Boolean continuesSongsAfterAdult;

    @Column(name = "answers_yes_no")
    private Boolean answersYesNo;
}