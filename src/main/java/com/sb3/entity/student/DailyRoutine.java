package com.sb3.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "daily_routine")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyRoutine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hour_06")
    private String hour06;
    @Column(name = "hour_07")
    private String hour07;
    @Column(name = "hour_08")
    private String hour08;
    @Column(name = "hour_09")
    private String hour09;
    @Column(name = "hour_10")
    private String hour10;
    @Column(name = "hour_11")
    private String hour11;
    @Column(name = "hour_12")
    private String hour12;
    @Column(name = "hour_13")
    private String hour13;
    @Column(name = "hour_14")
    private String hour14;
    @Column(name = "hour_15")
    private String hour15;
    @Column(name = "hour_16")
    private String hour16;
    @Column(name = "hour_17")
    private String hour17;
    @Column(name = "hour_18")
    private String hour18;
    @Column(name = "hour_19")
    private String hour19;
    @Column(name = "hour_20")
    private String hour20;
    @Column(name = "hour_21")
    private String hour21;
    @Column(name = "hour_22")
    private String hour22;
    @Column(name = "hour_23")
    private String hour23;
}