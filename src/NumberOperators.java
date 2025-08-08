
import java.util.Scanner;

public class NumberOperators {
public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);
    boolean running = true;
    while (running) {
        displayMenu();
        int choice = getMenuChoice(scanner);
        switch (choice) {
            case 1:
                System.out.println("Enter a number to check if it is prime:");
                int number = scanner.nextInt();
                if (isPrime(number)) {
                    System.out.println(number + " is a prime number.");
                } else {
                    System.out.println(number + " is not a prime number.");
                }
                break;
            case 2:
                System.out.println("Enter a number to reverse:");
                int numToReverse = scanner.nextInt();
                int reversedNumber = reverseNumber(numToReverse);
                System.out.println("Reversed number: " + reversedNumber);
                break;
            default:
            running = false;
                System.out.println("Exiting the program. Goodbye!");
                break;
        }

    }
    scanner.close();
}
private static void displayMenu() {
    System.out.println("\n Welcome to the Number Operators Menu");
    System.out.println("1. Check if a number is prime");
    System.out.println("2. Resverse a number");
    System.out.println("3. Exist");
    System.out.print("Please select an option (1-3): ");
}
private static int getMenuChoice(Scanner scanner) {
    while (!scanner.hasNextInt()) {
        System.out.print("Invalid input. Please enter a number between (1 ~ 3): ");
        scanner.next(); // Clear the invalid input
    }
    int choice = scanner.nextInt();
    
    while (choice < 1 || choice > 3) {
        System.out.print("Invalid choice. Please enter a number between (1 ~ 3): ");
        choice = scanner.nextInt();
    }
    return choice;
}
public static boolean isPrime(int number){
    if (number <= 1) {
        return false;
    }
    if (number == 2) {
        return true;
    }
    if (number % 2 == 0){
        return false;
    }
    for (int i = 3; i <= Math.sqrt(number); i+=2) {
        if (number % i == 0) {
            return false;
        }
    }
    return true;
}
public static int reverseNumber(int number) {
    int reversed = 0;
    while (number != 0) {
        int digit = number % 10;
        reversed = reversed * 10 + digit;
        number /= 10;
    }
    return reversed;
}

}
