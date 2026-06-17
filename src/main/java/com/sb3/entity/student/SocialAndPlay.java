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

    @Column(name = "how_plays_with_toys")
    private String howPlaysWithToys;

    @Column(name = "plays_with_adult")
    private String playsWithAdult;

    @Column(name = "repeats_game_actions_after_others")
    private Boolean repeatsGameActionsAfterOthers;

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

    @Column(name = "favorite_activities_and_toys")
    private String favoriteActivitiesAndToys;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "group_behavior_id")
    private GroupBehavior groupBehavior;
}