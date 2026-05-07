package com.tbs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tutor extends User {
    private String specialization;
    private int experienceYears;
    private double hourlyRate;

    public Tutor(String userId, String username, String name, String email, String password, String role,
                 String specialization, int experienceYears, double hourlyRate) {
        super(userId, username, name, email, password, role);
        this.specialization = specialization;
        this.experienceYears = experienceYears;
        this.hourlyRate = hourlyRate;
    }
}
