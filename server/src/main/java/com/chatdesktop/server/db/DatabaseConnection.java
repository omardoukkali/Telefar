package com.chatdesktop.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private static final String URL =
            System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5433/chatdb");
    private static final String USER = required("DB_USER");
    private static final String PASSWORD = required("DB_PASSWORD");

    private DatabaseConnection() {
    }

    private static String required(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Variable d'environnement manquante : " + name);
        }
        return value;
    }

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}