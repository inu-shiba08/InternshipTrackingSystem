package dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Central JDBC connection utility for the Internship Tracking System.
 *
 * Configuration is read from config.properties when present, then environment
 * variables. No database password is stored in source control.
 *
 * Supported keys:
 *   db.url
 *   db.user
 *   db.password
 *
 * Environment variable equivalents:
 *   DB_URL, DB_USER, DB_PASSWORD
 */
public final class DatabaseConnection {

    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/internship_tracking"
                    + "?useSSL=false&serverTimezone=UTC";

    private DatabaseConnection() {
        // Utility class.
    }

    public static Connection getConnection() throws SQLException {
        Properties properties = loadProperties();

        String url = firstNonBlank(
                properties.getProperty("db.url"),
                System.getenv("DB_URL"),
                DEFAULT_URL);

        String user = firstNonBlank(
                properties.getProperty("db.user"),
                System.getenv("DB_USER"),
                "root");

        String password = firstNonBlank(
                properties.getProperty("db.password"),
                System.getenv("DB_PASSWORD"),
                "");

        return DriverManager.getConnection(url, user, password);
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        Path configPath = Path.of("config.properties");

        if (Files.exists(configPath)) {
            try (InputStream input = Files.newInputStream(configPath)) {
                properties.load(input);
            } catch (IOException e) {
                throw new IllegalStateException(
                        "Unable to read config.properties.", e);
            }
        }

        return properties;
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return "";
    }

    /**
     * Small connectivity check useful before integrating the DAO layer.
     */
    public static boolean testConnection() {
        try (Connection ignored = getConnection()) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
