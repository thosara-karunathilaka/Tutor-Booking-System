package com.tbs.util;

import com.tbs.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileHandler {

    private static final String USER_FILE = "data/users.txt";
    private static final String STUDENT_FILE = "data/students.txt";
    private static final String TUTOR_FILE = "data/tutors.txt";
    private static final String ADMIN_FILE = "data/admin.txt";
    private static final String COURSE_FILE = "data/courses.txt";
    private static final String ENROLLMENT_FILE = "data/enrollments.txt";
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^U(\\d+)$");

    static {
        createFileIfNotExists(USER_FILE);
        createFileIfNotExists(STUDENT_FILE);
        createFileIfNotExists(TUTOR_FILE);
        createFileIfNotExists(ADMIN_FILE);
        createFileIfNotExists(COURSE_FILE);
        createFileIfNotExists(ENROLLMENT_FILE);
    }

    private static void createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Could not create file: " + filePath);
            }
        }
    }

    public static synchronized String generateNextUserId() {
        int maxId = 0;
        maxId = Math.max(maxId, findMaxUserIdInFile(USER_FILE));
        maxId = Math.max(maxId, findMaxUserIdInFile(STUDENT_FILE));
        maxId = Math.max(maxId, findMaxUserIdInFile(TUTOR_FILE));
        maxId = Math.max(maxId, findMaxUserIdInFile(ADMIN_FILE));
        return String.format("U%03d", maxId + 1);
    }

    public static void saveUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(USER_FILE, true), StandardCharsets.UTF_8))) {
            writer.write(toUserRecord(user));
            writer.newLine();
        } catch (IOException e) {
            throw new IllegalStateException("Error saving user", e);
        }
    }

    public static List<User> readUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(USER_FILE), StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if(line.isBlank()) continue;
                String[] d = line.split(",");
                if (d.length < 6) {
                    System.err.println("Malformed user row at line " + lineNumber + " in " + USER_FILE);
                    continue;
                }
                User u = new User(d[0], d[1], d[2], d[3], d[4], d[5]);
                users.add(u);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error reading users", e);
        }
        return users;
    }

    public static void saveStudent(Student p) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(STUDENT_FILE, true), StandardCharsets.UTF_8))) {
            writer.write(toStudentRecord(p));
            writer.newLine();
        } catch (IOException e) {
            throw new IllegalStateException("Error saving student", e);
        }
    }

    public static void saveTutor(Tutor d) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(TUTOR_FILE, true), StandardCharsets.UTF_8))) {
            writer.write(toTutorRecord(d));
            writer.newLine();
        } catch (IOException e) {
            throw new IllegalStateException("Error saving tutor", e);
        }
    }

    public static List<Student> readStudents() {
        List<Student> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(STUDENT_FILE), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.isBlank()) continue;
                String[] d = line.split(",");
                if (d.length >= 9) {
                    Student p = new Student(d[0], d[1], d[2], d[3], d[4], d[5], d[6], d[7], d[8]);
                    list.add(p);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error reading students", e);
        }
        return list;
    }

    public static List<Tutor> readTutors() {
        List<Tutor> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(TUTOR_FILE), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.isBlank()) continue;
                String[] d = line.split(",");
                if (d.length >= 9) {
                    Tutor doc = new Tutor(d[0], d[1], d[2], d[3], d[4], d[5], d[6], Integer.parseInt(d[7]), Double.parseDouble(d[8]));
                    list.add(doc);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error reading tutors", e);
        }
        return list;
    }

    public static void overwriteTutors(List<Tutor> tutors) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(TUTOR_FILE, false), StandardCharsets.UTF_8))) {
            for (Tutor t : tutors) {
                writer.write(toTutorRecord(t));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error writing tutors", e);
        }
    }
    
    public static void overwriteStudents(List<Student> students) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(STUDENT_FILE, false), StandardCharsets.UTF_8))) {
            for (Student s : students) {
                writer.write(toStudentRecord(s));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error writing students", e);
        }
    }

    public static void overwriteUsers(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(USER_FILE, false), StandardCharsets.UTF_8))) {
            for (User u : users) {
                writer.write(toUserRecord(u));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error writing users", e);
        }
    }

    public static void updateUser(User user) {
        List<User> users = readUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(user.getUserId())) {
                users.set(i, user);
                break;
            }
        }
        overwriteUsers(users);
    }

    public static void deleteUser(String userId) {
        List<User> users = readUsers();
        users.removeIf(u -> u.getUserId().equals(userId));
        overwriteUsers(users);
    }

    public static void updateStudent(Student student) {
        List<Student> students = readStudents();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getUserId().equals(student.getUserId())) {
                students.set(i, student);
                break;
            }
        }
        overwriteStudents(students);
    }

    public static void deleteStudent(String userId) {
        List<Student> students = readStudents();
        students.removeIf(s -> s.getUserId().equals(userId));
        overwriteStudents(students);
    }

    public static void updateTutor(Tutor tutor) {
        List<Tutor> tutors = readTutors();
        for (int i = 0; i < tutors.size(); i++) {
            if (tutors.get(i).getUserId().equals(tutor.getUserId())) {
                tutors.set(i, tutor);
                break;
            }
        }
        overwriteTutors(tutors);
    }

    public static void deleteTutor(String userId) {
        List<Tutor> tutors = readTutors();
        tutors.removeIf(t -> t.getUserId().equals(userId));
        overwriteTutors(tutors);
    }

    public static synchronized String generateNextCourseId() {
        int max = 0;
        File file = new File(COURSE_FILE);
        if (!file.exists()) return "C001";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            Pattern p = Pattern.compile("^C(\\d+)$");
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] cols = line.split(",", 2);
                Matcher m = p.matcher(cols[0].trim());
                if (m.matches()) {
                    int numeric = Integer.parseInt(m.group(1));
                    if (numeric > max) max = numeric;
                }
            }
        } catch (IOException e) {}
        return String.format("C%03d", max + 1);
    }

    public static synchronized String generateNextEnrollmentId() {
        int max = 0;
        File file = new File(ENROLLMENT_FILE);
        if (!file.exists()) return "E001";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            Pattern p = Pattern.compile("^E(\\d+)$");
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] cols = line.split(",", 2);
                Matcher m = p.matcher(cols[0].trim());
                if (m.matches()) {
                    int numeric = Integer.parseInt(m.group(1));
                    if (numeric > max) max = numeric;
                }
            }
        } catch (IOException e) {}
        return String.format("E%03d", max + 1);
    }

    public static void saveCourse(Course course) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(COURSE_FILE, true), StandardCharsets.UTF_8))) {
            String safeTitle = course.getTitle() != null ? course.getTitle().replace(",", "%2C") : "";
            String safeDesc = course.getDescription() != null ? course.getDescription().replace(",", "%2C") : "";
            writer.write(String.join(",", course.getCourseId(), course.getTutorId(), safeTitle, safeDesc, String.valueOf(course.getPrice())));
            writer.newLine();
        } catch (IOException e) {}
    }

    public static List<Course> readCourses() {
        List<Course> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(COURSE_FILE), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.isBlank()) continue;
                String[] d = line.split(",");
                if (d.length >= 5) {
                    list.add(new Course(d[0], d[1], d[2].replace("%2C", ","), d[3].replace("%2C", ","), Double.parseDouble(d[4])));
                }
            }
        } catch (IOException e) {}
        return list;
    }

    public static void saveEnrollment(Enrollment enrollment) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ENROLLMENT_FILE, true), StandardCharsets.UTF_8))) {
            writer.write(String.join(",", enrollment.getEnrollmentId(), enrollment.getStudentId(), enrollment.getCourseId()));
            writer.newLine();
        } catch (IOException e) {}
    }

    public static List<Enrollment> readEnrollments() {
        List<Enrollment> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ENROLLMENT_FILE), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.isBlank()) continue;
                String[] d = line.split(",");
                if (d.length >= 3) {
                    list.add(new Enrollment(d[0], d[1], d[2]));
                }
            }
        } catch (IOException e) {}
        return list;
    }

    private static String toUserRecord(User user) {
        return String.join(",", user.getUserId(), user.getUsername(), user.getName(), user.getEmail(), user.getPassword(), user.getRole());
    }

    private static String toStudentRecord(Student student) {
        return String.join(",", student.getUserId(), student.getUsername(), student.getName(), student.getEmail(), student.getPassword(), student.getRole(), student.getGradeLevel(), student.getAddress(), student.getPhoneNumber());
    }

    private static String toTutorRecord(Tutor tutor) {
        return String.join(",", tutor.getUserId(), tutor.getUsername(), tutor.getName(), tutor.getEmail(), tutor.getPassword(), tutor.getRole(), tutor.getSpecialization(), String.valueOf(tutor.getExperienceYears()), String.valueOf(tutor.getHourlyRate()));
    }

    private static int findMaxUserIdInFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return 0;
        int max = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] columns = line.split(",", 2);
                String userId = columns[0].trim();
                Matcher matcher = USER_ID_PATTERN.matcher(userId);
                if (matcher.matches()) {
                    int numeric = Integer.parseInt(matcher.group(1));
                    if (numeric > max) max = numeric;
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error reading user IDs from " + filePath, e);
        }
        return max;
    }
}
