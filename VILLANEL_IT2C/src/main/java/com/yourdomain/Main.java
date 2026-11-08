package com.yourdomain;

import com.yourdomain.dao.DatabaseInitializer;
import com.yourdomain.model.User;
import com.yourdomain.ui.AdminMenu;
import com.yourdomain.ui.Authentication;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();
        Scanner scanner = new Scanner(System.in);
        Authentication auth = new Authentication();
        AdminMenu adminMenu = new AdminMenu();

        boolean running = true;
        while (running) {
            System.out.println("\n=== CLINIC APPOINTMENT SYSTEM ===");
            System.out.println("1. Login");
            System.out.println("2. Register Account");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    User currentUser = auth.login(scanner);
                    if (currentUser != null) {
                        if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
                            adminMenu.showMenu(scanner, currentUser);
                        } else {
                            System.out.println("Regular user dashboard logged in.");
                        }
                    }
                    break;
                case 2:
                    auth.register(scanner);
                    break;
                case 3:
                    System.out.println("Exiting application...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
        scanner.close();
    }
}