package com.example.demo.grademanagementsystem;

import java.util.List;

class Student {
    private String studentId;
    private String name;
    private List<Double> scores;
    private double averageGrade;

    public Student(String studentId, String name, List<Double> grades) {
        this.studentId = studentId;
        this.name = name;
        this.scores = grades;
        this.averageGrade = calculateAverage();
    }

    private double calculateAverage() {
        if (scores == null || scores.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Double grade : scores) {
            sum += grade;
        }
        return sum / scores.size();
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public List<Double> getGrades() {
        return scores;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Scores: %s, Average: %.2f",
                studentId, name, scores, averageGrade);
    }
}
