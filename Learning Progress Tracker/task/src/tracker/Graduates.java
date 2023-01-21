package tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graduates {
    //stores student id, followed by the name of the courses graduated
    private static final Map<String, ArrayList<String>> notifiedGraduates = new HashMap<>();

    public static void notifyGraduates() {
        int studentsNotified = 0;
        for (Student student : Student.students.values()) {
            boolean graduated = false;
            String id = student.getId();
            for (var course : Statistics.getCourses()) {
                String courseName = course.getName();
                if (notifiedGraduates.containsKey(id) && notifiedGraduates.get(id).contains(courseName))
                    continue;
                if (student.getGrade(courseName) == course.getCompletionPoints()) {
                    printNotification(id, courseName);
                    graduated = true;
                    if (notifiedGraduates.containsKey(id)) {
                        notifiedGraduates.get(id).add(courseName);
                    }
                    else {
                        notifiedGraduates.put(id, new ArrayList<>());
                        notifiedGraduates.get(id).add(courseName);
                    }
                }
            }
            studentsNotified += graduated ? 1 : 0;
        }
        System.out.println("Total " + studentsNotified + " students have been notified.");
    }

    private static void printNotification(String id, String courseName) {
        Student student = Student.findStudent(id);
        System.out.printf("To: %s\n", student.getEmail());
        System.out.print("Re: Your Learning Progress\n");
        System.out.printf("Hello, %s! You have accomplished our %s course!\n", student.getFullName(), courseName);
    }
}
