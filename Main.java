import dao.DatabaseConnection;
import ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        // Initialize SQLite database and tables automatically
        DatabaseConnection.initializeDatabase();

        // Start console UI interaction loop
        ConsoleUI consoleUI = new ConsoleUI();
        consoleUI.start();
    }
}