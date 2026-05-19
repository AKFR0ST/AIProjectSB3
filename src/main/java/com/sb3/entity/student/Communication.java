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

    @Column(name = "how_understands_wants", columnDefinition = "text")
    private String howUnderstandsWants;

    @Column(name = "request_method")
    private String requestMethod;

    @Column(name = "request_help")
    private String requestHelp;

    @Column(name = "report_pain")
    private String reportPain;

    @Column(name = "request_attention")
    private String requestAttention;

    @Column(name = "reaction_to_delay")
    private String reactionToDelay;

    @Column(name = "eye_contact")
    private Boolean eyeContact;

    @Column(name = "understands_pointing")
    private Boolean understandsPointing;

    @Column(name = "understands_gaze")
    private Boolean understandsGaze;

    @Column(name = "responds_to_name")
    private Boolean respondsToName;

    @Column(name = "name_used")
    private String nameUsed;

    @Column(name = "understands_instructions")
    private String understandsInstructions;

    @Column(name = "understands_contextual_instructions")
    private Boolean understandsContextualInstructions;

    @Column(name = "can_imitate_sound")
    private Boolean canImitateSound;

    @Column(name = "spontaneous_sounds_frequency")
    private String spontaneousSoundsFrequency;

    @Column(name = "spontaneous_sounds_examples")
    private String spontaneousSoundsExamples;

    @Column(name = "labels_objects_actions")
    private Boolean labelsObjectsActions;

    @Column(name = "label_count")
    private Integer labelCount;

    @Column(name = "completes_songs")
    private Boolean completesSongs;

    @Column(name = "answers_questions")
    private Boolean answersQuestions;
}