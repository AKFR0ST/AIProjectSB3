package com.sb3.entity.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "relative")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Relative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String relation;
    @Column(name = "full_name")
    private String fullName;
    private String phone;
    private String email;
}