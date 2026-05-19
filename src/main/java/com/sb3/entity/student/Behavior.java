package com.sb3.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "behavior")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Behavior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "repetitive_behavior_id")
    private BehaviorDetail repetitiveBehavior;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "unwanted_behavior_id")
    private BehaviorDetail unwantedBehavior;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dangerous_behavior_id")
    private BehaviorDetail dangerousBehavior;
}