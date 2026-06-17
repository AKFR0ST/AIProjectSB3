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

    @Column(name = "initiates_peer_contact")
    private Boolean initiatesPeerContact;

    @Column(name = "supports_peer_dialogue")
    private Boolean supportsPeerDialogue;

    @Column(name = "symbolic_play_with_peers_5min")
    private Boolean symbolicPlayWithPeers5min;

}