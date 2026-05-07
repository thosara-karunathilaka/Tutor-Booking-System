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
}
