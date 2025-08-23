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
// Manager subclass
class Manager extends Employee {

    public Manager(String name, int id, double salary) {
         // Call the parent class (Employee) constructor
        super(name, id, salary);
    }
    
    @Override
    public double calculateBonus() {
        // Managers get 20% bonus
        return salary * 0.20;
    }
}
