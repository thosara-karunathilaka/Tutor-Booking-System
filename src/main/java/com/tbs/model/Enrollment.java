package com.tbs.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Enrollment {
    private String enrollmentId;
    private String studentId;
    private String courseId;
    private String status; // ACTIVE or COMPLETED

    public Enrollment(String enrollmentId, String studentId, String courseId) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.status = "ACTIVE";
    }

    public Enrollment(String enrollmentId, String studentId, String courseId, String status) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.status = status;
    }

    public boolean isCompleted() {
        return "COMPLETED".equals(this.status);
    }
}
