package com.tbs.service;

import com.tbs.model.Course;
import com.tbs.model.Enrollment;
import com.tbs.util.FileHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    public List<Course> getAllCourses() {
        return FileHandler.readCourses();
    }

    public Course getCourseById(String id) {
        return getAllCourses().stream().filter(c -> c.getCourseId().equals(id)).findFirst().orElse(null);
    }

    public void saveCourse(Course course) {
        course.setCourseId(FileHandler.generateNextCourseId());
        FileHandler.saveCourse(course);
    }

    public List<Course> getCoursesByTutor(String tutorId) {
        return getAllCourses().stream().filter(c -> c.getTutorId().equals(tutorId)).collect(Collectors.toList());
    }

    public void enrollStudent(String studentId, String courseId) {
        Enrollment e = new Enrollment(FileHandler.generateNextEnrollmentId(), studentId, courseId);
        FileHandler.saveEnrollment(e);
    }

    public List<Enrollment> getEnrollmentsByCourse(String courseId) {
        return FileHandler.readEnrollments().stream().filter(e -> e.getCourseId().equals(courseId)).collect(Collectors.toList());
    }

    public List<Enrollment> getEnrollmentsByStudent(String studentId) {
        return FileHandler.readEnrollments().stream().filter(e -> e.getStudentId().equals(studentId)).collect(Collectors.toList());
    }
}
