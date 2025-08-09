/**  
 * Lab Activity 2
 * Write a calculator program with 
 * - Add(),subtract(),multiply(),divide() methods
 * - Main method to display a menu and call the methods based on user input
 * - Exit option to terminate the program
 */
import java.util.Scanner;
public class CalculatorProgram {
public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    boolean running = true;

    while (running){
            displayMenu();
            int choice = getMenuChoice(scanner);
            // Exist option
            if (choice == 5){
                running = false;
                System.out.println("Exiting the program. Goodbye!");
                continue;
            }
            // Do operation based on user choice
            double[] operands = getOperands(scanner);
            double result = performOperation(choice, operands[0], operands[1]);
            displayResult(result);

        } 
            
   scanner.close(); 
}

private static void displayMenu() {
    System.out.println("Welcome to the Calculator Program!");

    System.out.println("1. Addition");
    System.out.println("2. Subtraction");
    System.out.println("3. Multiplication");
    System.out.println("4. Division");
    System.out.println("5. Exit");
    System.out.print("Please select an operation(1~5):");
}
private static int getMenuChoice(Scanner scanner) {
  
    while (!scanner.hasNextInt()) {
        System.out.print("Invalid input. Please enter a number between 1 and 5: ");
        scanner.next(); // Clear the invalid input
    }
    int choice = scanner.nextInt();
    
    while (choice< 1 || choice > 5) 
    {
        System.out.print("Invalid choice. Please enter a number between 1 and 5: ");
        choice = scanner.nextInt();    
    }
    return choice;
}
private static double [] getOperands(Scanner scanner){
    System.out.print("Enter the first number: ");

    while (!scanner.hasNextDouble()) {
        System.out.print("Invalid input. Please enter a valid number: ");
        scanner.next(); // Clear the invalid input
        
    }
    double firstNumber = scanner.nextDouble();
    
    System.out.print("Enter the second number: ");
    while (!scanner.hasNextDouble()) {
        System.out.print("Invalid input. Please enter a valid number: ");
        scanner.next(); // Clear the invalid input
        
    }
    double secondNumber = scanner.nextDouble();

    return new double[]{firstNumber, secondNumber};
}
private static double performOperation(int choice, double firstnumber, double secondNumber) {
    switch (choice) {
        case 1 : 
            return add(firstnumber, secondNumber);
        case 2 :
            return subtract(firstnumber, secondNumber);
        case 3 :
            return multiply(firstnumber, secondNumber);
        case 4 :
            return divide(firstnumber, secondNumber);
        default:
           return 0;
    }
    
}
private static void displayResult(double result){
    System.out.printf("Result : %.2f%n", result);
}

public static double add(double a, double b) {
    return a + b;
}
public static double subtract(double a, double b) {
    return a - b;
}
public static double multiply(double a, double b) {
    return a * b;
}
public static double divide(double a, double b) {
    if (b == 0) {
        System.out.println("Error: Division by zero is not allowed.");
        return Double.NaN; // Return Not-a-Number for division by zero
    }
    return a / b;
}


}
