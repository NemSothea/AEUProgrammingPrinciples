package com.example.demo.secureloginsystem;

import com.example.demo.secureloginsystem.servics.SecureLoginSystem;

public class Main {
      public static void main(String[] args) {
        SecureLoginSystem loginSystem = new SecureLoginSystem();
        
        // Add some sample users (in real system, this would be done through admin interface)
        try {
            // loginSystem.addUser("admin", "admin123");
    
        } catch (Exception e) {
            System.out.println("Note: " + e.getMessage());
        }

        loginSystem.startLoginSession();
    }
}
