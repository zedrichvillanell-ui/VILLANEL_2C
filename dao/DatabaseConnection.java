package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:clinic_system.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL, " +
                "status TEXT NOT NULL" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);

            // Insert default active Admin account if not exists
            String insertAdmin = "INSERT OR IGNORE INTO users (username, password, role, status) VALUES ('admin', 'admin123', 'ADMIN', 'ACTIVE');";
            stmt.execute(insertAdmin);

        } catch (SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
        }
    }
}