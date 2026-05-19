package com.sb3.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "preferred")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Preferred {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "food_drinks")
    private String foodDrinks;

    private String activities;
    private String games;
    private String places;
    private String sensations;
}