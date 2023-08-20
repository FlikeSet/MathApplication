package parts;

import java.util.Scanner;

public class UserInput {
    private final Scanner scanner;

    public UserInput() {
        scanner = new Scanner(System.in);
    }

    public String readInputEquation() {
        System.out.print("Enter a equation (type 'exit' to quit): ");
        return scanner.nextLine();
    }
    public String readInputRoot() {
        System.out.print("Enter a root (type 'exit' to quit): ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid double value.");
            scanner.next(); // Consume invalid input
        }

        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }
}