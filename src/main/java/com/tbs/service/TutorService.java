package com.tbs.service;

import com.tbs.model.Tutor;
import com.tbs.util.FileHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorService {

    public List<Tutor> getAllTutors() {
        return FileHandler.readTutors();
    }

    public void saveTutor(Tutor tutor) {
        tutor.setUserId(FileHandler.generateNextUserId());
        FileHandler.saveUser(tutor);
        FileHandler.saveTutor(tutor);
    }

    public Tutor getTutorById(String id) {
        return getAllTutors().stream().filter(t -> t.getUserId().equals(id)).findFirst().orElse(null);
    }

    public void updateTutor(Tutor tutor) {
        FileHandler.updateUser(tutor);
        FileHandler.updateTutor(tutor);
    }

    public void deleteTutor(String id) {
        FileHandler.deleteUser(id);
        FileHandler.deleteTutor(id);
    }
}
