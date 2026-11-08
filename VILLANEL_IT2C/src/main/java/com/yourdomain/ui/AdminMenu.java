package com.yourdomain.ui;

import com.yourdomain.dao.ActivityLogDao;
import com.yourdomain.dao.UserDao;
import com.yourdomain.model.ActivityLog;
import com.yourdomain.model.User;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final UserDao userDao = new UserDao();
    private final ActivityLogDao logDao = new ActivityLogDao();

    public void showMenu(Scanner scanner, User admin) {
        int choice = -1;
        while (choice != 5) {
            System.out.println("\n--- ADMIN DASHBOARD ---");
            System.out.println("1. View All System Users");
            System.out.println("2. View Pending Registrations");
            System.out.println("3. Approve User Account");
            System.out.println("4. Update User Role & Status");
            System.out.println("5. View System Audit Logs");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    printUserTable("ALL SYSTEM USERS", userDao.getAllUsers());
                    break;
                case 2:
                    printUserTable("PENDING REGISTRATIONS", userDao.getPendingUsers());
                    break;
                case 3:
                    printUserTable("PENDING REGISTRATIONS", userDao.getPendingUsers());
                    System.out.print("Enter User ID to approve (or 0 to cancel): ");
                    int approveId = Integer.parseInt(scanner.nextLine());
                    if (approveId != 0) {
                        userDao.approveUser(admin.getId(), approveId);
                        System.out.println("Success! User ID " + approveId + " is now APPROVED.");
                    }
                    break;
                case 4:
                    printUserTable("ALL USERS FOR UPDATE", userDao.getAllUsers());
                    System.out.print("Enter Target User ID to update (or 0 to cancel): ");
                    int targetId = Integer.parseInt(scanner.nextLine());
                    if (targetId == 0) break;

                    System.out.print("Enter new role (STUDENT, TEACHER, CLINIC_STAFF, ADMIN): ");
                    String newRole = scanner.nextLine().trim();
                    System.out.print("Enter new status (APPROVED, PENDING): ");
                    String newStatus = scanner.nextLine().trim();

                    userDao.updateUserRoleAndStatus(admin.getId(), targetId, newRole, newStatus);
                    System.out.println("Success! User ID " + targetId + " updated to Role: " + newRole + ", Status: " + newStatus);
                    break;
                case 5:
                    showAuditLogMenu(scanner);
                    break;
                case 6:
                    System.out.println("Logging out...");
                    choice = 5;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void showAuditLogMenu(Scanner scanner) {
        System.out.println("\n--- AUDIT LOG MENU ---");
        System.out.println("1. View All System Logs");
        System.out.println("2. Filter Logs by User ID");
        System.out.print("Choose an option: ");

        int logChoice;
        try {
            logChoice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (logChoice == 1) {
            printLogTable(logDao.getAllLogs());
        } else if (logChoice == 2) {
            System.out.print("Enter User ID to filter: ");
            try {
                int userId = Integer.parseInt(scanner.nextLine());
                printLogTable(logDao.getLogsByUserId(userId));
            } catch (NumberFormatException e) {
                System.out.println("Invalid User ID.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void printUserTable(String title, List<User> users) {
        System.out.println("\n--- " + title + " ---");
        System.out.printf("%-4s | %-12s | %-12s | %-10s\n", "ID", "Username", "Role", "Status");
        System.out.println("--------------------------------------------------");
        for (User u : users) {
            System.out.printf("%-4d | %-12s | %-12s | %-10s\n", u.getId(), u.getUsername(), u.getRole(), u.getStatus());
        }
    }

    private void printLogTable(List<ActivityLog> logs) {
        System.out.println("\n--- SYSTEM AUDIT LOGS ---");
        System.out.printf("%-5s | %-8s | %-18s | %-35s | %-20s\n", "ID", "User ID", "Action Type", "Description", "Timestamp");
        System.out.println("-----------------------------------------------------------------------------------------");
        for (ActivityLog log : logs) {
            System.out.printf("%-5d | %-8d | %-18s | %-35s | %-20s\n",
                    log.id(), log.userId(), log.actionType(), log.description(), log.timestamp());
        }
    }
}