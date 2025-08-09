
/**
 * Lab activity Contact Manger 
- Use HashMap<String,String> for Name -> Phone 
- Add, Search, Remove, And Display contacts 
- Use lambda with forEach for display 
- Principle tie-in : Each Feature in its own method(modularity)

*/
import java.util.HashMap;
import java.util.Scanner;

public class ContactManager {

    // MARK:- Properties
    // HashMap to store contacts with name as key and phone number as value.
    private static HashMap<String, String> contacts = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            // Display the menu options.
            displayMenu();

            int choice = scanner.nextInt();
            // When user selects an option.
            running = selectChoice(choice);

        }
        // Close the scanner to prevent resource leaks.
        scanner.close();
    }

    // MARK:- Method to display the menu options
    private static void displayMenu() {

        System.out.println("\n--- Contact Manager ---");
        System.out.println("1. Add Contact");
        System.out.println("2. Search Contact");
        System.out.println("3. Remove Contact");
        System.out.println("4. Display All Contacts");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }

    // MARK:- Select Choice method to handle user input
    /**
     * @param choice
     * @param running
     */
    private static boolean selectChoice(int choice) {
        switch (choice) {
            case 1:
                addContact();
                break;
            case 2:
                searchContact();
                break;
            case 3:
                removeContact();
                break;
            case 4:
                displayAllContacts();
                break;
            case 5:
                System.out.println("Exiting the Contact Manager.");
                return false; // Exit the loop
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        return true;
    }

    // MARK:- Add Contact
    private static void addContact() {
        System.out.print("Enter name: ");
        String name = scanner.next();
        System.out.print("Enter phone number: ");
        String phone = scanner.next();
        contacts.put(name, phone);
        System.out.println("Contact added successfully.");
    }
// MARK:- Search Contact
    private static void searchContact() {
        System.out.print("Enter name to search: ");
        String name = scanner.next();
        if (contacts.containsKey(name)) {
            System.out.println("Phone number: " + contacts.get(name));
        } else {
            System.out.println("Contact not found.");
        }
    }

    // MARK:- Remove Contact
    private static void removeContact() {
        System.out.print("Enter name to remove: ");
        String name = scanner.next();
        if (contacts.remove(name) != null) {
            System.out.println("Contact removed successfully.");
        } else {
            System.out.println("Contact not found.");
        }
    }

    // MARK:- Display All Contacts
    private static void displayAllContacts() {
        System.out.println("\n--- All Contacts ---");
        if (contacts.isEmpty()) {
            System.out.println("No contacts available.");
            return;
        }
        // Using lambda expression to display all contacts.
        contacts.forEach((name, phone) -> System.out.println(name + ": " + phone));
    }
}
