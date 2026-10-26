
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnexion {

    private static final String URL =
        "jdbc:mysql://localhost:3306/gessto6?useSSL=false&serverTimezone=UTC";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "";

    private static MyConnexion instance;
    private static Connection connection;

    private MyConnexion() {
        try {
            connection = DriverManager.getConnection(
                URL, LOGIN, PASSWORD
            );
            System.out.println("Connexion MySQL réussie !");
        } catch (SQLException e) {
            System.err.println("Erreur de connexion MySQL : "
                + e.getMessage());
        }
    }

    public static synchronized MyConnexion getInstance() {
        if (instance == null ||
            connection == null || isConnectionClosed()) {
            instance = new MyConnexion();
        }
        return instance;
    }

    private static boolean isConnectionClosed() {
        try {
            return connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    public static Connection getConnection() {
        getInstance();
        return connection;
    }
}
