package tracker;

import java.util.LinkedHashMap;
import java.util.Map;

class Student {
    static int ID_Count = 0;
    static final int ID_PREFIX = 2023;
    static Map<String, Student> students = new LinkedHashMap<>();

    private final String id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final int[] grades = {0, 0, 0, 0};

    public Student(String firstName, String lastName, String email) {
        this.id = generateID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        students.putIfAbsent(this.id, this);
    }

    public String getEmail() {
        return this.email;
    }
    public String getId() { return this.id; }

    void addPoints(int[] points) {
        for (int i = 0; i < 4; i++) {
            this.grades[i] += points[i];
        }
    }

    void printStudentGrades() {
        System.out.printf("%s points: Java=%s; DSA=%s; Databases=%s; Spring=%s",
                id, grades[0], grades[1], grades[2], grades[3]);
    }

    int getGrade(String courseName) {
        int courseIndex = nameToCourseIndex(courseName.toLowerCase());
        if (courseIndex != -1) return this.grades[courseIndex];
        else return -1;
    }

    String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public static void listStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found");
            return;
        }
        System.out.println("Students:");
        students.keySet().forEach(System.out::println);
    }

    public static boolean existingStudent(String id) {
        return students.containsKey(id);
    }

    public static Student findStudent(String id) {
        return students.getOrDefault(id, null);
    }

    public static boolean uniqueEmail(String email) {
        for (Student s : students.values()) {
            if (s.getEmail().equals(email)) return false;
        }
        return true;
    }

    private static String generateID() {
        ID_Count++;
        return ID_PREFIX + "/" + String.format("%05d", ID_Count);
    }

    private static int nameToCourseIndex(String name) {
        return switch(name) {
            case "java" -> 0;
            case "dsa" -> 1;
            case "databases" -> 2;
            case "spring" -> 3;
            default -> -1;
        };
    }
}