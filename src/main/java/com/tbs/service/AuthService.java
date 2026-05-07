package com.tbs.service;

import com.tbs.model.User;
import com.tbs.util.FileHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    public User login(String username, String password) {
        List<User> users = FileHandler.readUsers();
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public void registerUser(User user) {
        user.setUserId(FileHandler.generateNextUserId());
        FileHandler.saveUser(user);
        if ("STUDENT".equalsIgnoreCase(user.getRole())) {
            com.tbs.model.Student student = new com.tbs.model.Student(user.getUserId(), user.getUsername(), user.getName(), user.getEmail(), user.getPassword(), user.getRole(), "N/A", "N/A", "N/A");
            FileHandler.saveStudent(student);
        } else if ("TUTOR".equalsIgnoreCase(user.getRole())) {
            com.tbs.model.Tutor tutor = new com.tbs.model.Tutor(user.getUserId(), user.getUsername(), user.getName(), user.getEmail(), user.getPassword(), user.getRole(), "N/A", 0, 0.0);
            FileHandler.saveTutor(tutor);
        }
    }
}
