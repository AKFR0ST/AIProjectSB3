package com.sb3.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "group_behavior")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupBehavior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sits_at_table_duration_min")
    private Integer sitsAtTableDurationMin;

    @Column(name = "stands_in_line")
    private Boolean standsInLine;

    @Column(name = "waits_turn")
    private Boolean waitsTurn;

    @Column(name = "responds_to_group_instructions")
    private Boolean respondsToGroupInstructions;

    @Column(name = "follows_instructions_for_part_of_group")
    private Boolean followsInstructionsForPartOfGroup;

    @Column(name = "imitates_peers")
    private Boolean imitatesPeers;

    @Column(name = "shares")
    private Boolean shares;

    @Column(name = "accepts_rejection_calmly")
    private Boolean acceptsRejectionCalmly;
}