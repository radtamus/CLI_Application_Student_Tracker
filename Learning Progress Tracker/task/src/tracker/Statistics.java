package tracker;

import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
/*
This class holds a map(?) of the courses name with values being:
1. Number of submissions
2. Grade total obtained collectively by students
3. A set of id's of enrolled students

Whenever a student submission is added, the number of submission will increase by 1
and the grade total will increase by the student's grade. The student id is also
added to the set of enrolled students.
 */
public class Statistics {
    private static final Map<String, Statistics> courses = initializeCourses();

    private static Map<String, Statistics> initializeCourses() {
        Map<String, Statistics> courses = new HashMap<>();
        courses.put("java", new Statistics("Java", 600));
        courses.put("dsa", new Statistics("DSA", 400));
        courses.put("databases", new Statistics("Databases", 480));
        courses.put("spring", new Statistics("Spring", 550));

        return courses;
    }

    private final String name;
    private final int completionPoints;
    private int submissions = 0;
    private int gradeTotal = 0;
    private final Set<String> enrolledStudents = new HashSet<>();

    public Statistics(String name, int completionPoints) {
        this.name = name;
        this.completionPoints = completionPoints;
    }

    public static Collection<Statistics> getCourses() {return courses.values();}
    public static void studentSubmission(String id, int[] grades) {
        for (int i = 0; i < grades.length; i++) {
            if (grades[i] == 0) continue;
            String course = indexToCourseName(i);
            courses.get(course).addSubmission(grades[i], id);
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static void printStatistics() {
        Comparator[] compareCriteria = {new ComparePopularity(), new CompareActivity(),
                new CompareGradeAverage()};
        String[] mostText = {"Most popular", "Highest activity", "Easiest course"};
        String[] leastText = {"Least popular", "Lowest activity", "Hardest course"};
        //compare by popularity
        for (int i = 0; i < compareCriteria.length; i++) {
            Comparator<Statistics> comparator = compareCriteria[i];
            Statistics maxCourse = courses.values().stream().max(comparator).get();
            Statistics minCourse = courses.values().stream().min(comparator).get();
            //checks if we have empty data
            if (comparator.compare(maxCourse, new Statistics("empty", 0)) == 0) {
                System.out.println(mostText[i] + ": " + "n/a");
                System.out.println(leastText[i] + ": "+ "n/a");
                continue;
            }

            String[] minCourses = courses.values().stream()
                    .filter(x -> comparator.compare(x, minCourse) == 0)
                    .map(Statistics::getName).toArray(String[]::new);
            String[] maxCourses = courses.values().stream()
                    .filter(x -> comparator.compare(x, maxCourse) == 0)
                    .map(Statistics::getName).toArray(String[]::new);

            //check if courses qualify for most and least at the same time
            if (comparator.compare(minCourse, maxCourse) == 0) {
                System.out.print(mostText[i] + ": ");
                printArray(maxCourses);
                System.out.println(leastText[i] + ": n/a");
                continue;

            }
            System.out.print(mostText[i] + ": ");
            printArray(maxCourses);
            System.out.print(leastText[i] + ": ");
            printArray(minCourses);
        }
    }

    public static void printCourseStatistics(String courseName) {
        if (!courses.containsKey(courseName)) {
            System.out.println("Unknown course");
            return;
        }
        Statistics course = courses.get(courseName);
        System.out.println(course.getName());
        System.out.printf("%-10s %-10s %-10s\n", "id", "points", "completed");

        Comparator<String> comparator = (s1, s2) -> {
            int grade1 = course.getStudentGrade(s1);
            int grade2 = course.getStudentGrade(s2);
            if (grade1 == grade2) {
                return s1.compareTo(s2);
            } else return -1 * Integer.compare(grade1, grade2);
        };
        course.enrolledStudents.stream()
              .sorted(comparator)
              .forEach(s -> System.out.printf("%-10s %-10s %-10s\n",
                      s, course.getStudentGrade(s), round(course.getCompletion(s))+"%%"));
    }

    private static double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private static void printArray(String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) System.out.print(", ");
        }
        System.out.println();
    }

    private static String indexToCourseName(int i) {
        return switch (i) {
            case 0 -> "java";
            case 1 -> "dsa";
            case 2 -> "databases";
            case 3 -> "spring";
            default -> null;
        };
    }

    private void addSubmission(int grade, String id) {
        this.submissions++;
        this.gradeTotal += grade;
        this.enrolledStudents.add(id);
    }

    private int getStudentGrade(String id) {
        return Student.findStudent(id).getGrade(this.getName());
    }

    private double getCompletion(String id) {
        return this.getStudentGrade(id) * 100.0 / this.getCompletionPoints();
    }

    public int getStudentNumber() {
        return this.enrolledStudents.size();
    }

    public int getSubmissions() {
        return this.submissions;
    }

    public int getGradeTotal() {
        return this.gradeTotal;
    }

    public String getName() {
        return this.name;
    }

    public int getCompletionPoints() {
        return this.completionPoints;
    }

    public double getGradeAverage() {
        if (this.getSubmissions() == 0) return 0d;
        return (double) this.getGradeTotal() / this.getSubmissions();
    }
}

class ComparePopularity implements Comparator<Statistics> {
    @Override
    public int compare(Statistics s1, Statistics s2) {
        return Integer.compare(s1.getStudentNumber(), s2.getStudentNumber());
    }
}

class CompareActivity implements Comparator<Statistics> {
    @Override
    public int compare(Statistics s1, Statistics s2) {
        return Integer.compare(s1.getSubmissions(), s2.getSubmissions());
    }
}

class CompareGradeAverage implements Comparator<Statistics> {
    @Override
    public int compare(Statistics s1, Statistics s2) {
        return Double.compare(s1.getGradeAverage(), s2.getGradeAverage());
    }
}

//class CompareStudentIds implements Comparator<String> {
//    @Override
//    public int compare(String s1, String s2) {
//        if (Student.findStudent(s1).)
//    }
//}

