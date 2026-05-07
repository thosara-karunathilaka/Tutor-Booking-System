package com.tbs.scratch;
import com.tbs.util.FileHandler;
public class Test {
    public static void main(String[] args) {
        System.out.println("Tutors: " + FileHandler.readTutors());
        System.out.println("Students: " + FileHandler.readStudents());
    }
}
