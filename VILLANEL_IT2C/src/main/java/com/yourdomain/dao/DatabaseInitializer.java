package com.yourdomain.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL, " +
                "status TEXT NOT NULL DEFAULT 'PENDING');";

        String activityLogsTable = "CREATE TABLE IF NOT EXISTS activity_logs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "action_type TEXT NOT NULL, " +
                "description TEXT NOT NULL, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE);";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(usersTable);
            stmt.execute(activityLogsTable);

            // Auto-repair missing status column if reusing existing database file
            try {
                stmt.execute("ALTER TABLE users ADD COLUMN status TEXT NOT NULL DEFAULT 'PENDING';");
            } catch (SQLException ignored) {}

            String defaultAdmin = "INSERT OR IGNORE INTO users (username, password, role, status) VALUES ('admin', 'admin123', 'ADMIN', 'ACTIVE');";
            stmt.execute(defaultAdmin);

        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }
}