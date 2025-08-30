package com.example.demo.grademanagementsystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class GradeManagementSystem {
    private final CustomLogger logger;
    private List<Student> students;

    public GradeManagementSystem() {
        this.logger = CustomLogger.getInstance();
        students = new ArrayList<>();
        logger.logInfo("Grade management system initialized");
    }

    public void readGradesFromCSV(String filename) {
        try {
            logger.logInfo("Attempting to read from file: " + filename);

            File csvFile = new File(filename);
            logger.logInfo("File object created: " + csvFile.getAbsolutePath());

            // CREATE FILE IF IT DOESN'T EXIST
            if (!csvFile.exists()) {
                logger.logInfo("File not found. Creating sample CSV file...");
                // createSampleCSV(filename);

                // Reinitialize the file object after creation
                csvFile = new File(filename);

                // Verify file was created
                if (!csvFile.exists()) {
                    throw new FileNotFoundException("Failed to create CSV file");
                }
                logger.logInfo("Sample CSV file created successfully");
            }

            // NOW READ THE FILE - it should exist now
            if (csvFile.length() == 0) {
                throw new IOException("CSV file is empty");
            }

            logger.logInfo("Reading file content...");
            logger.logInfo("File size: " + csvFile.length() + " bytes");

            List<Student> tempStudents = new ArrayList<>();
            int lineNumber = 0;

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                String line;

                // Read and log the first line to verify content
                if ((line = reader.readLine()) != null) {
                    logger.logInfo("First line: " + line);
                    lineNumber++;
                }

                // Reset reader to start from beginning
                reader.close();
            }

            // Now read properly with header processing
            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                String line;
                boolean isHeaderProcessed = false;

                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    line = line.trim();

                    if (line.isEmpty()) {
                        continue;
                    }

                    logger.logInfo("Processing line " + lineNumber + ": " + line);

                    String[] values = line.split(",");

                    if (!isHeaderProcessed) {
                        isHeaderProcessed = true;
                        logger.logInfo("Skipping header line");
                        continue;
                    }

                    if (values.length < 3) {
                        logger.logWarning("Invalid data format at line " + lineNumber);
                        continue;
                    }

                    try {
                        String studentId = values[0].trim();
                        String name = values[1].trim();
                        List<Double> grades = new ArrayList<>();

                        for (int i = 2; i < values.length; i++) {
                            try {
                                double grade = Double.parseDouble(values[i].trim());
                                grades.add(grade);
                            } catch (NumberFormatException e) {
                                logger.logWarning("Invalid grade format at line " + lineNumber);
                            }
                        }

                        Student student = new Student(studentId, name, grades);
                        tempStudents.add(student);
                        logger.logInfo("Successfully processed student: " + studentId);

                    } catch (Exception e) {
                        logger.logError("Error processing line " + lineNumber, e);
                    }
                }
            }

            if (tempStudents.isEmpty()) {
                throw new IOException("No valid student records found in CSV");
            }

            students = tempStudents;
            logger.logInfo("Successfully read " + students.size() + " student records");

        } catch (FileNotFoundException e) {
            logger.logError("File not found after creation attempt", e);
            throw new RuntimeException("File operation failed: " + e.getMessage());
        } catch (IOException e) {
            logger.logError("I/O error during file reading", e);
            throw new RuntimeException("I/O error: " + e.getMessage());
        } catch (Exception e) {
            logger.logError("Unexpected error during file processing", e);
            throw new RuntimeException("Unexpected error: " + e.getMessage());
        }
    }
    public void readTestGradesFromCSV(String filename) {
    try {
        students.clear();
        logger.logInfo("Cleared existing student data");
        logger.logInfo("Reading from file: " + filename);
        
        File csvFile = new File(filename);
        
        // Check if file exists
        if (!csvFile.exists()) {
            throw new FileNotFoundException("CSV file not found: " + csvFile.getAbsolutePath());
        }

        // Read the file
        List<Student> tempStudents = new ArrayList<>();
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isHeaderProcessed = false;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty()) continue;

                String[] values = line.split(",");

                // Skip header line (first line)
                if (!isHeaderProcessed) {
                    isHeaderProcessed = true;
                    continue;
                }

                // Process student data
                if (values.length >= 3) {
                    String studentId = values[0].trim();
                    String name = values[1].trim();
                    List<Double> grades = new ArrayList<>();

                    for (int i = 2; i < values.length; i++) {
                        try {
                            double grade = Double.parseDouble(values[i].trim());
                            grades.add(grade);
                        } catch (NumberFormatException e) {
                            logger.logWarning("Invalid grade format at line " + lineNumber);
                        }
                    }
                    

                    Student student = new Student(studentId, name, grades);
                    tempStudents.add(student);
                }
            }
        }

        students = tempStudents;
        logger.logInfo("Successfully read " + students.size() + " students from CSV");

    } catch (IOException e) {
        logger.logError("Error reading CSV file", e);
        throw new RuntimeException("Cannot read file: " + e.getMessage());
    }
}

    public void createSampleCSV(String filename) {
        try {
            logger.logInfo("Creating sample CSV file: " + filename);

            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                writer.println("student_id,name,grade1,grade2,grade3,grade4");
                writer.println("S001,John Doe,85.5,92.0,78.5,88.0");
                writer.println("S002,Jane Smith,91.0,89.5,94.0,87.5");
                writer.println("S003,Bob Johnson,76.0,82.5,79.0,85.0");
                writer.println("S004,Alice Brown,88.0,92.5,95.0,90.5");
                writer.println("S005,Charlie Wilson,65.0,72.5,68.0,70.0");

                // Flush the writer to ensure data is written
                writer.flush();
            }

            logger.logInfo("Sample CSV file created successfully");

        } catch (IOException e) {
            logger.logError("Failed to create sample CSV file", e);
            throw new RuntimeException("Failed to create CSV file: " + e.getMessage());
        }
    }
    public void clearStudents() {
    students.clear();
    logger.logInfo("Cleared student data - ready for fresh read");
}

    public void calculateAverages() {
        logger.logInfo("Calculating averages for all students");
        for (Student student : students) {
            logger.logInfo("Average for " + student.getName() + ": " +
                    String.format("%.2f", student.getAverageGrade()));
        }
    }

    public void writeResultsToFile(String filename) {
        try {
            logger.logInfo("Writing results to file: " + filename);

            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                writer.println("Student Grade Results");
                writer.println("=====================");
                writer.println("Generated on: " + LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                writer.println();

                writer.printf("%-15s %-20s %-30s %-10s%n",
                        "Student ID", "Name", "Grades", "Average");
                writer.println("-".repeat(75));

                for (Student student : students) {
                    writer.printf("%-15s %-20s %-30s %-10.2f%n",
                            student.getStudentId(),
                            student.getName(),
                            student.getGrades().toString(),
                            student.getAverageGrade());
                }

                writer.println();
                writer.println("Summary:");
                writer.println("Total students: " + students.size());

                // Calculate overall statistics
                double totalAverage = 0.0;
                for (Student student : students) {
                    totalAverage += student.getAverageGrade();
                }
                totalAverage = students.isEmpty() ? 0.0 : totalAverage / students.size();

                writer.printf("Overall average: %.2f%n", totalAverage);
            }

            logger.logInfo("Successfully wrote results to " + filename);

        } catch (IOException e) {
            logger.logError("Failed to write results file", e);
            throw new RuntimeException("Failed to write results: " + e.getMessage());
        }
    }

    public void displayResults() {
        System.out.println("\n=== Student Grade Results ===");
        for (Student student : students) {
            System.out.println(student);
        }

        // Display summary
        double totalAverage = 0.0;
        for (Student student : students) {
            totalAverage += student.getAverageGrade();
        }
        totalAverage = students.isEmpty() ? 0.0 : totalAverage / students.size();

        System.out.printf("\nSummary: %d students, Overall average: %.2f%n",
                students.size(), totalAverage);
    }

    public List<Student> getStudents() {
        return students;
    }
}
