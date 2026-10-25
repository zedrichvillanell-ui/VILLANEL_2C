package ui;

import dao.UserDao;
import model.User;

import java.util.Scanner;

public class Authentication {
    private final UserDao userDao;
    private final Scanner scanner;

    public Authentication(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void start() {
        while (true) {
            System.out.println("\n=== CLINIC APPOINTMENT SYSTEM ===");
            System.out.println("1. Login");
            System.out.println("2. Register Account");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleRegistration();
                    break;
                case "3":
                    System.out.println("Exiting application. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleRegistration() {
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
        String roleChoice = scanner.nextLine();

        String role = "STUDENT";
        if (roleChoice.equals("2")) {
            role = "CLINIC_STAFF";
        } else if (roleChoice.equals("3")) {
            role = "ADMIN";
        }

        User newUser = new User(username, password, role, "PENDING");
        boolean success = userDao.registerUser(newUser);

        if (success) {
            System.out.println("Registration successful! Account status is PENDING approval.");
        } else {
            System.out.println("Registration failed. Please try a different username.");
        }
    }

    private void handleLogin() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = userDao.login(username, password);

        if (user == null) {
            System.out.println("Invalid username or password.");
            return;
        }

        if ("PENDING".equals(user.getStatus()) && !"ADMIN".equals(user.getRole())) {
            System.out.println("Your account is still PENDING approval by an administrator.");
            return;
        }

        System.out.println("Login successful! Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
        showDashboard(user);
    }

    private void showDashboard(User user) {
        while (true) {
            System.out.println("\n--- " + user.getRole() + " DASHBOARD ---");
            if ("STUDENT".equals(user.getRole())) {
                System.out.println("1. Request Appointment");
                System.out.println("2. View My Appointments");
                System.out.println("3. Logout");
            } else if ("CLINIC_STAFF".equals(user.getRole())) {
                System.out.println("1. Manage Student Appointments");
                System.out.println("2. View Clinic Records");
                System.out.println("3. Logout");
            } else if ("ADMIN".equals(user.getRole())) {
                AdminMenu adminMenu = new AdminMenu(userDao, scanner);
                adminMenu.showMenu();
                break;
            }

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            if (choice.equals("3") || choice.equalsIgnoreCase("logout")) {
                System.out.println("Logging out session...");
                break;
            } else {
                System.out.println("Feature ready for next implementation phase.");
            }
        }
    }
}