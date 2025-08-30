/**
 * 2024-08-23 Saturday
 ** Exercise - Employee Management System
 * 
 * Create an Employee Hierarchy
 * - Abstract Employee class with: name, id, salary
 * - Abstract method: calculateBonus()
 * - Concrete method: displayDetails()
 * - Subclasses: Manager, Developer, Salesperson
 * - Each has different bonus calculation logic
 * - Demonstrate polymorphism in main()
 * 
 * Reflection Questions
 * - How do inheritance and polymorphism improve code maintainability?
 * - Which SOLID principles did you apply?
 */
package com.example.demo.employeemanagementsystem;
// Abstract Employee class
public abstract class Employee {

    // Private fields
    protected String name;
    protected int id;
    protected double salary;

    // Abstract method for bonus calculation
    public abstract double calculateBonus();

    // Constructor to initialize all fields
    public Employee(String name, int id, double salary) {
        this.name = name;
        this.id = id;
        this.salary = salary;
    }

    // Concrete method to display employee details
    public void displayDetails() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Salary: $" + salary);
        System.out.println("Bonus: $" + calculateBonus());
        System.out.println("Total Compensation: $" + (salary + calculateBonus()));
        System.out.println("------------------------");
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public double getSalary() {
        return salary;
    }
}
