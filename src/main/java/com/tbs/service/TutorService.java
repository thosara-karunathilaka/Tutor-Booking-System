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
}
