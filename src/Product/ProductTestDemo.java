package Product;
/*16-08-2025: Lab Activity 
## Create a Product Class
- Private fields: name, price, quantity
- Constructor to initialize all fields
- Public methods: displayInfo(), updatePrice(), isInStock()
- Getter methods for accessing private fields
- Create 3 product objects and test all methods
- Focus: Apply encapsulation and observe how it improves data protection
 * 
 * Test class to demonstrate the Product class
 * 
*/

public class ProductTestDemo {
    public static void main(String[] args) {
        // Create 3 product objects
        Product product1 = new Product("Laptop", 999.99, 5);
        Product product2 = new Product("Mouse", 25.50, 0);
        Product product3 = new Product("Keyboard", 75.00, 10);
        
        // Test displayInfo method
        System.out.println("Product Information:");
        product1.displayInfo();
        product2.displayInfo();
        product3.displayInfo();
        
        // Test updatePrice method
        product1.updatePrice(899.99);
        product2.updatePrice(-10.00); // This should show an error
        
        // Test isInStock method
        System.out.println("\nStock Status:");
        System.out.println(product1.getName() + " in stock: " + product1.isInStock());
        System.out.println(product2.getName() + " in stock: " + product2.isInStock());
        System.out.println(product3.getName() + " in stock: " + product3.isInStock());
        
        // Test getter methods
        System.out.println("\nUsing Getter Methods:");
        System.out.println(product1.getName() + " price: $" + product1.getPrice());
        System.out.println(product3.getName() + " quantity: " + product3.getQuantity());
    }
    
}