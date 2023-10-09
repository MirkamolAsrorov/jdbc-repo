package org.example;

import java.sql.*;

public class Main {
    // Get a connection to the database.
    static Connection conn;
    static PreparedStatement preparedStatement;

    static {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "postgres", "root123");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SQLException {


        showCustomersWithTheirTransactions();

        conn.close();

    }

    private static void showCustomersWithTheirTransactions() throws SQLException {
        System.out.println("JOINS");

        preparedStatement = conn.prepareStatement("SELECT transaction_id, amount, first_name, last_name FROM transactions AS t" +
                "INNER JOIN customers AS c ON t.customer_id = c.customer_id;");

        ResultSet resultSet2 = preparedStatement.executeQuery();
        while (resultSet2.next()) {
            System.out.print(resultSet2.getString("transaction_id") + "\t");
            System.out.print(resultSet2.getString("amount") + "\t");
            System.out.print(resultSet2.getString("first_name") + "\t");
            System.out.print(resultSet2.getString("last_name") + "\t");
        }

        preparedStatement.close();
    }

    private static void showACustomerWhereTheFirstName(String first_name) throws SQLException {
        // Create a statement object.
        preparedStatement = conn.prepareStatement("SELECT * FROM customers WHERE first_name = ?");

        // Set the SQL statement parameters.
        preparedStatement.setString(1, first_name);

        // Execute the SQL statement.
        ResultSet rs = preparedStatement.executeQuery();

        // Process the results of the SQL statement.
        while (rs.next()) {
            System.out.println(rs.getString("customer_id"));
            System.out.println(rs.getString("first_name"));
            System.out.println(rs.getString("last_name"));
        }

        // Close the statement and connection objects.
        preparedStatement.close();
    }

    private static void showTransactionWhereId(Integer transaction_id) throws SQLException {
        System.out.println("Transactions\n");
        preparedStatement = conn.prepareStatement("SELECT * FROM transactions WHERE transaction_id = ?");
        preparedStatement.setInt(1, 1);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.print(resultSet.getString("transaction_id") + "\t");
            System.out.print(resultSet.getString("amount") + "\t");
            System.out.println(resultSet.getString("customer_id") + "\t");
        }

        preparedStatement.close();
    }
}