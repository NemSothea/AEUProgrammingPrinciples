package com.example.demo.grademanagementsystem;
/*
 * 
 * Create a Student Grade Management System
Build a robust student grade management system
- Read student grades from csv file (student.csv)
- Calculate average grade for each student
- write results to output file(result.txt)
- handle all possible exceptions gracefully
- Create log file for all operations

*/
public class StudentGradeManagementSystem {

    private static final String CSV_FOLDER = "src/main/java/com/example/demo/grademanagementsystem/";
    private static final String CSV_FILE = CSV_FOLDER + "students.csv";

    public static void main(String[] args) {
        CustomLogger logger = CustomLogger.getInstance();

        try {
            logger.logInfo("Starting student grade management system");

            GradeManagementSystem system = new GradeManagementSystem();

            system.clearStudents();
            // Read grades from CSV
            system.readGradesFromCSV(CSV_FILE);

            // Calculate averages
            system.calculateAverages();

            // Write results to output file
            system.writeResultsToFile("src/main/java/com/example/demo/grademanagementsystem/results.txt");

            // Display results on console
            system.displayResults();

            logger.logInfo("Grade management process completed successfully");

        } catch (Exception e) {
            logger.logError("Critical error in main method", e);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}