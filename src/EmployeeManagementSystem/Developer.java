
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

public class Developer extends Employee {

    public Developer(String name, int id, double salary) {
        // Call the parent class (Employee) constructor
        super(name, id, salary);
    }

    // Implement the abstract method with specific logic for Developers
    @Override
    public double calculateBonus() {
        // Developers get a 15% bonus
        return salary * 0.15;
    }
}