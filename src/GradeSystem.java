
// 2 Aug 2025
// Lab Activity 1
/**
 *  Lab Activity 
 * Write a grade system 
 * - Input : exam score
 * - Output : grade A,B,C,D,F from 1-100 but skip mutiple of 3
 */
import java.util.Scanner;

public class GradeSystem {
    public static void main(String[] args) {
        // Additional functionality can be added here
        // For example, initializing a grade book, adding students, etc.
        System.out.println("Welcome to the Grade System!");
        Scanner scanner = new Scanner(System.in);
        // Path 1 : Get exam score
        int examScore = getExamScore(scanner);
        char grade = calculateGrade(examScore);
        printGrade(grade);
        printSkipMultipleOfThree();
        
        scanner.close();
      
    }
    /**
     * @param scanner
     * @return
     */
    private static int getExamScore(Scanner scanner) {
        System.out.print("Enter exam score (0 ~ 100): ");
        int score = scanner.nextInt();
        // Validate input
        while (score < 0 || score > 100) {
            System.out.print("Invalid score. Please enter a score between 0 and 100: ");
            score = scanner.nextInt();
        }
        return score;
    }
    private static char calculateGrade(int score) {
        if (score >= 90) return 'A';
        if (score >= 80) return 'B';
        if (score >= 70) return 'C';
        if (score >= 60) return 'D';
        if (score >= 50) return 'E';
        return 'F';
    }
    private static void printGrade(char grade) {
        System.out.println("Your grade is: " + grade);
    }
    private static void printSkipMultipleOfThree() {
       System.out.println("\n Number from 1 to 50 (skipping multiples of 3):");
       for (int i = 1; i <= 50; i++) {
           if (i % 3 == 0) {
               continue; // Skip multiples of 3 
           }
          System.out.print(i+" "); 
        }
    }
}
