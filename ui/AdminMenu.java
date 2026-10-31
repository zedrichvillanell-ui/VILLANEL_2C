package ui;

import dao.UserDao;
import model.User;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final UserDao userDao;
    private final Scanner scanner;
    private final User currentAdmin;

    public AdminMenu(UserDao userDao, Scanner scanner, User currentAdmin) {
        this.userDao = userDao;
        this.scanner = scanner;
        this.currentAdmin = currentAdmin;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- ADMIN DASHBOARD ---");
            System.out.println("1. View All System Users");
            System.out.println("2. View Pending Registrations");
            System.out.println("3. Approve User Account");
            System.out.println("4. Update User Role & Status");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewAllUsers();
                    break;
                case "2":
                    viewPendingUsers();
                    break;
                case "3":
                    approveUserAccount();
                    break;
                case "4":
                    updateUserRoleAndStatus();
                    break;
                case "5":
                    System.out.println("Logging out admin session...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewAllUsers() {
        List<User> users = userDao.getAllUsers();
        printUserTable(users, "ALL SYSTEM USERS");
    }

    private void viewPendingUsers() {
        List<User> users = userDao.getPendingUsers();
        printUserTable(users, "PENDING REGISTRATIONS");
    }

    private void approveUserAccount() {
        List<User> pendingUsers = userDao.getPendingUsers();
        if (pendingUsers.isEmpty()) {
            System.out.println("\nNo pending registrations found.");
            return;
        }

        printUserTable(pendingUsers, "PENDING ACCOUNTS FOR APPROVAL");
        int userId = readIntInput("Enter Target User ID to approve (or 0 to cancel): ");

        if (userId == 0) {
            System.out.println("Approval action cancelled.");
            return;
        }

        boolean exists = pendingUsers.stream().anyMatch(u -> u.getId() == userId);
        if (!exists) {
            System.out.println("Error: User ID " + userId + " not found in pending registrations.");
            return;
        }

        boolean success = userDao.approveUser(userId);
        if (success) {
            System.out.println("Success! User ID " + userId + " status updated to APPROVED.");
        } else {
            System.out.println("Failed to approve user. Please try again.");
        }
    }

    private void updateUserRoleAndStatus() {
        List<User> users = userDao.getAllUsers();
        printUserTable(users, "ALL USERS FOR UPDATE");

        int userId = readIntInput("Enter Target User ID to update (or 0 to cancel): ");
        if (userId == 0) {
            System.out.println("Update action cancelled.");
            return;
        }

        // Self-Edit Restriction Rule
        if (currentAdmin != null && userId == currentAdmin.getId()) {
            System.out.println("WARNING: Modifying the currently active admin account is restricted.");
            return;
        }

        boolean exists = users.stream().anyMatch(u -> u.getId() == userId);
        if (!exists) {
            System.out.println("Error: User ID " + userId + " does not exist.");
            return;
        }

        String newRole = readValidRole();
        String newStatus = readValidStatus();

        boolean success = userDao.updateUserRoleAndStatus(userId, newRole, newStatus);
        if (success) {
            System.out.println("Success! User ID " + userId + " updated to Role: " + newRole + ", Status: " + newStatus);
        } else {
            System.out.println("Failed to update user. Please try again.");
        }
    }

    private int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input. Please enter numbers only.");
            }
        }
    }

    private String readValidRole() {
        while (true) {
            System.out.print("Enter new role (STUDENT, TEACHER, CLINIC_STAFF, ADMIN): ");
            String role = scanner.nextLine().trim().toUpperCase();
            if (role.equals("STUDENT") || role.equals("TEACHER") || role.equals("CLINIC_STAFF") || role.equals("ADMIN")) {
                return role;
            }
            System.out.println("Invalid role. Please enter one of: STUDENT, TEACHER, CLINIC_STAFF, ADMIN.");
        }
    }

    private String readValidStatus() {
        while (true) {
            System.out.print("Enter new status (APPROVED, PENDING): ");
            String status = scanner.nextLine().trim().toUpperCase();
            if (status.equals("APPROVED") || status.equals("PENDING")) {
                return status;
            }
            System.out.println("Invalid status. Please enter APPROVED or PENDING.");
        }
    }

    private void printUserTable(List<User> users, String title) {
        System.out.println("\n--- " + title + " ---");
        if (users.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        String format = "%-5s | %-15s | %-12s | %-10s%n";
        System.out.printf(format, "ID", "Username", "Role", "Status");
        System.out.println("--------------------------------------------------");
        for (User u : users) {
            System.out.printf(format, u.getId(), u.getUsername(), u.getRole(), u.getStatus());
        }
    }
}