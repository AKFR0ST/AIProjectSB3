package com.sb3.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "peer_interactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeerInteractions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parallel_play")
    private Boolean parallelPlay;

    @Column(name = "physical_contact_initiation")
    private Boolean physicalContactInitiation;

    @Column(name = "spontaneous_requests_to_peers")
    private Boolean spontaneousRequestsToPeers;

    @Column(name = "responds_to_peer_requests")
    private Boolean respondsToPeerRequests;

    @Column(name = "symbolic_play_with_peers_5min")
    private Boolean symbolicPlayWithPeers5min;

    @Column(name = "social_imaginative_play_with_peers")
    private Boolean socialImaginativePlayWithPeers;

    @Column(name = "dialogue_multiple_phrases_5topics")
    private Boolean dialogueMultiplePhrases5topics;
}