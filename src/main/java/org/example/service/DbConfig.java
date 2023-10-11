package org.example.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfig {
    public static Connection getConnection() {
        String url = "jdbc:postgresql://localhost:5432/test";
        String user = "postgres";
        String password = "root123";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }
}
