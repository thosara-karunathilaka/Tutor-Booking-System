package com.tbs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private String courseId;
    private String tutorId;
    private String title;
    private String description;
    private double price;
    private String tutorName;

    public Course(String courseId, String tutorId, String title, String description, double price) {
        this.courseId = courseId;
        this.tutorId = tutorId;
        this.title = title;
        this.description = description;
        this.price = price;
    }
}
