package com.tbs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Student extends User {
    private String gradeLevel;
    private String address;
    private String phoneNumber;

    public Student(String userId, String username, String name, String email, String password, String role,
                   String gradeLevel, String address, String phoneNumber) {
        super(userId, username, name, email, password, role);
        this.gradeLevel = gradeLevel;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
