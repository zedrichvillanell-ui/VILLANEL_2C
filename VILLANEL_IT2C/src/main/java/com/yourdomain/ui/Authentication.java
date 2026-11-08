package com.yourdomain.ui;

import com.yourdomain.dao.ActivityLogDao;
import com.yourdomain.dao.UserDao;
import com.yourdomain.model.User;
import java.util.Scanner;

public class Authentication {
    private final UserDao userDao = new UserDao();
    private final ActivityLogDao logDao = new ActivityLogDao();

    public User login(Scanner scanner) {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = userDao.authenticateUser(username, password);

        if (user != null) {
            if ("PENDING".equalsIgnoreCase(user.getStatus())) {
                System.out.println("Your account is still PENDING approval by an administrator.");
                return null;
            }
            logDao.logAction(user.getId(), "USER_LOGIN", "User " + user.getUsername() + " (ID: " + user.getId() + ") logged in.");
            return user;
        } else {
            System.out.println("Invalid username or password.");
            return null;
        }
    }

    public void register(Scanner scanner) {
        System.out.println("\n--- REGISTER ACCOUNT ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.println("Select Role:");
        System.out.println("1. STUDENT");
        System.out.println("2. CLINIC_STAFF");
        System.out.println("3. ADMIN");
        System.out.print("Choose role (1-3): ");

        String role = "STUDENT";
        try {
            int roleChoice = Integer.parseInt(scanner.nextLine());
            if (roleChoice == 2) role = "CLINIC_STAFF";
            else if (roleChoice == 3) role = "ADMIN";
        } catch (NumberFormatException e) {
            System.out.println("Invalid selection, defaulting to STUDENT.");
        }

        if (userDao.registerUser(username, password, role)) {
            System.out.println("Registration successful! Account status is PENDING approval.");
        } else {
            System.out.println("Registration failed. Username may already exist.");
        }
    }
}