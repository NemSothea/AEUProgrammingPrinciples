
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

package EmployeeManagementSystem;

public class EmployeeManagementSystem {
    public static void main(String[] args) {

        // Create an array of Employee references.
        // This is polymorphism: we are storing different subclass objects
        // in a reference of the parent type (Employee).
        Employee[] employees = {
                new Manager("Pisey", 12, 2000),
                new Developer("Sothea", 13, 1200),
                new Salesperson("Seyha", 16, 13000),
                new Manager("Srey Nich", 20, 1500)
        };

        System.out.println("Employee Management System");
        System.out.println("==========================");

        // Loop through the array and call displayDetails() on each employee.
        // The JVM automatically calls the correct calculateBonus() method
        // for each specific object (Runtime Polymorphism).
        for (Employee emp : employees) {
            emp.displayDetails();
        }
    }
}
