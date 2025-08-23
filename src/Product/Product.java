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
 * 
 * 
*/

public class Product {
    // Private fields
    private String name;
    private double price;
    private int quantity;

    // Constructor to initialize all fields
    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Public method to display product information
    public void displayInfo() {
        System.out.println("Product: " + name);
        System.out.println("Price: $" + price);
        System.out.println("Quantity: " + quantity);
        System.out.println("In stock: " + (isInStock() ? "Yes" : "No"));
        System.out.println("----------------------");
    }

    // Public method to update price
    public void updatePrice(double newPrice) {
        if (newPrice >= 0) {
            this.price = newPrice;
            System.out.println(name + " price updated to: $" + newPrice);
        } else {
            System.out.println("Error: Price cannot be negative");
        }
    }

    // Public method to check if product is in stock
    public boolean isInStock() {
        return quantity > 0;
    }

    // Getter methods for accessing private fields
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
