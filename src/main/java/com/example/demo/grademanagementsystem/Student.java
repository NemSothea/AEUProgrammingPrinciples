package com.example.demo.grademanagementsystem;

import java.util.List;

class Student {
    private String studentId;
    private String name;
    private List<Double> grades;
    private double averageGrade;

    public Student(String studentId, String name, List<Double> grades) {
        this.studentId = studentId;
        this.name = name;
        this.grades = grades;
        this.averageGrade = calculateAverage();
    }

    private double calculateAverage() {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Double grade : grades) {
            sum += grade;
        }
        return sum / grades.size();
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public List<Double> getGrades() {
        return grades;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Grades: %s, Average: %.2f",
                studentId, name, grades, averageGrade);
    }
}
