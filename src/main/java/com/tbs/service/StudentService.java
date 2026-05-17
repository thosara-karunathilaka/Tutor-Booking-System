package com.tbs.service;

import com.tbs.model.Student;
import com.tbs.util.FileHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {


    public List<Student> getAllStudents() {
        return FileHandler.readStudents();
    }

    public void saveStudent(Student student) {
        student.setUserId(FileHandler.generateNextUserId());
        FileHandler.saveUser(student);
        FileHandler.saveStudent(student);
    }

    public Student getStudentById(String id) {
        return getAllStudents().stream().filter(s -> s.getUserId().equals(id)).findFirst().orElse(null);
    }

    public void updateStudent(Student student) {
        FileHandler.updateUser(student);
        FileHandler.updateStudent(student);
    }
    public void deleteStudent(String id) {
        FileHandler.deleteUser(id);
        FileHandler.deleteStudent(id);
    }
}
