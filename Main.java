import dao.DatabaseInitializer;
import dao.UserDao;
import ui.Authentication;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize Database & Tables automatically via DatabaseInitializer
        DatabaseInitializer.initializeDatabase();

        // 2. Instantiate shared DAOs and utilities
        UserDao userDao = new UserDao();
        Scanner scanner = new Scanner(System.in);

        // 3. Delegate execution flow to Authentication UI
        Authentication auth = new Authentication(userDao, scanner);
        auth.start();
    }
}