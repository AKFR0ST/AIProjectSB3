package com.sb3.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "social_and_play")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialAndPlay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attention_to_toys")
    private String attentionToToys;

    @Column(name = "independent_play_duration")
    private String independentPlayDuration;

    @Column(name = "imitative_play")
    private Boolean imitativePlay;

    @Column(name = "free_time_activities")
    private String freeTimeActivities;

    @Column(name = "walk_activities")
    private String walkActivities;

    @Column(name = "eye_contact_with_peers")
    private Boolean eyeContactWithPeers;

    @Column(name = "interest_in_peers")
    private String interestInPeers;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "peer_interactions_id")
    private PeerInteractions peerInteractions;

    @Column(name = "favorite_games")
    private String favoriteGames;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "group_behavior_id")
    private GroupBehavior groupBehavior;
}