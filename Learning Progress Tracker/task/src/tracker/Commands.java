package tracker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


class Commands {
    private static final Map<String, Runnable> commands = initializeCommands();

    private static Map<String, Runnable> initializeCommands() {
        Map<String, Runnable> commands = new HashMap<>();
        commands.put("", Commands::emptyInput);
        commands.put("exit", Commands::exit);
        commands.put("add students", Commands::addStudents);
        commands.put("back", Commands::back);
        commands.put("list", Commands::list);
        commands.put("add points", Commands::addPoints);
        commands.put("find", Commands::find);
        commands.put("statistics", Commands::statistics);
        commands.put("notify", Commands::notifyGraduates);

        return commands;
    }

    public static void executeCommand(String input) {
        commands.getOrDefault(input, Commands::unknownCommands).run();
    }

    private static void emptyInput() {
        System.out.println("No input");
    }

    private static void exit() {
        System.out.println("Bye!");
        System.exit(0);
    }

    private static void back() {
        System.out.println("Enter 'exit' to exit the program.");
    }

    private static void addStudents() {
        final String INVALID_NAME = ".+[^a-zA-z'\\- ].+|.*['-]{2,}.*|.|[-'].+|.+[-']";
        final String VALID_EMAIL = "[^@]*@[^@]*\\.[^@]*";
        final String VALID_COMPONENTS = ".* .* .*";

        String studentFirstName;
        String studentLastName;
        String studentEmail;

        System.out.println("Enter student credentials or 'back' to return:");
        int studentCounter = 0;
        String input;
        while (true) {
            input = Main.readLine();
            if (input.equals("back")) break;

            //Check if there are 3 strings that could be the credentials
            if (!input.matches(VALID_COMPONENTS)) {
                System.out.println("Incorrect credentials.");
                continue;
            }

            //Check First Name
            studentFirstName = input.substring(0, input.indexOf(' '));
            if (studentFirstName.matches(INVALID_NAME)) {
                System.out.println("Incorrect first name.");
                continue;
            }

            //Check Last Name
            studentLastName = input.substring(input.indexOf(' ') + 1, input.lastIndexOf(" "));
            if (studentLastName.matches(INVALID_NAME)) {
                System.out.println("Incorrect last name.");
                continue;
            }

            //Check email
            studentEmail = input.substring(input.lastIndexOf(' ') + 1);
            if (!studentEmail.matches(VALID_EMAIL)) {
                System.out.println("Incorrect email.");
                continue;
            }
            if (!Student.uniqueEmail(studentEmail)) {
                System.out.println("This email is already taken.");
                continue;
            }

            //If we reached this point, all the credentials are valid
            new Student(studentFirstName, studentLastName, studentEmail);
            System.out.println("The student has been added.");
            studentCounter++;
        }
        //back has been typed, ending the loop
        System.out.println("Total " + studentCounter + " students have been added.");

    }

    private static void unknownCommands() {
        System.out.println("Unknown command!");
    }

    private static void list() {
        Student.listStudents();
    }

    private static void addPoints() {
        final String VALID_NUMBER_FORMAT = "[^ ]+( [0-9]+){4}";
        System.out.println("Enter an id and points or 'back' to return");
        String input;
        while (true) {
            input = Main.readLine();

            if (input.equals("back")) break;

            if (!input.matches(VALID_NUMBER_FORMAT)) {
                System.out.println("Incorrect points format.");
                continue;
            }

            String id = input.substring(0, input.indexOf(" "));
            if (!Student.existingStudent(id)) {
                System.out.printf("No student is found for id=%s\n", id);
                continue;
            }

            int[] points = Arrays.stream(input.substring(input.indexOf(" ") + 1).split(" "))
                    .mapToInt(Integer::parseInt).toArray();

            Student.findStudent(id).addPoints(points);
            Statistics.studentSubmission(id, points);
            System.out.println("Points updated.");
        }
    }

    private static void find() {
        System.out.println("Enter an id or 'back' to return");
        String input;

        while (true) {
            input = Main.readLine();

            if (input.equals("back")) break;

            if (!Student.existingStudent(input)) {
                System.out.printf("No student is found for id=%s\\n", input);
                continue;
            }
            Student.findStudent(input).printStudentGrades();
        }

    }

    private static void statistics() {
        System.out.println("Type the name of a course to see details or 'back' to quit:");
        Statistics.printStatistics();
        String input;

        while (true) {
            input = Main.readLine();

            if (input.equals("back")) break;

            Statistics.printCourseStatistics(input);
        }
    }

    private static void notifyGraduates() {
        Graduates.notifyGraduates();
    }
}
