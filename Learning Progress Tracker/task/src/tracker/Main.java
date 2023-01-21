package tracker;

import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        commandLoop();
    }

    private static void commandLoop() {
        System.out.println("Learning Progress Tracker");
        String input;
        while (scanner.hasNextLine()) {
            input = readLine();
            Commands.executeCommand(input);
        }
    }

    public static String readLine() {
        return scanner.nextLine().strip().toLowerCase();
    }
}

