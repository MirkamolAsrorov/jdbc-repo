package org.example.service;

import org.example.model.Customer;
import org.example.model.TransactionWithCustomer;
import org.example.model.TransactionWithCustomerId;

import javax.swing.plaf.PanelUI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DbService {

    public static void navigateData(){
        String sql = "SELECT * FROM customers LIMIT 10";
        Connection connection = DbConfig.getConnection();
        try {
            Statement statement
                    = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(sql);
            System.out.println("10 rows of the table");

            while (resultSet.next()) {
                display(resultSet);

            }

            System.out.println("Now, Go to the 2nd row");
            resultSet.absolute(2);
            display(resultSet);

            System.out.println("Now, Go to the Previous row - 1st row");
            resultSet.previous();
            display(resultSet);
            resultSet.close();

            statement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void display(ResultSet resultSet){

        try {
            System.out.print("customer_id " + resultSet.getLong(1));
            System.out.print(" firstName " + resultSet.getString(2));
            System.out.println(" lastName " + resultSet.getString(3));
            System.out.println();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }



    public static void batchThreeOperations(String sql, String sql2, String sql3 ){
        Connection connection = DbConfig.getConnection();
        try {
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.execute(sql);
            statement.addBatch(sql);

            statement.execute(sql2);
            statement.addBatch(sql2);

            statement.execute(sql3);
            statement.addBatch(sql3);

            connection.commit();

            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void addCustomers() throws SQLException {
        Connection connection = DbConfig.getConnection();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO customers(first_name, last_name) VALUES('Federico1234', 'Peter232')");
            statement.executeUpdate("INSERT INTO customers(first_name, last_name) VALUES('Pol', 'Alien')");

            // if any error occurs, the savepoint will not be committed .
            Savepoint savepoint1 = connection.setSavepoint("My savePoint");
            statement.executeUpdate("INSERT INTO customers(first_name, last_name) VALUES('Fe', 'Pe')");

            try {
                statement.executeUpdate("INSERT INTO customers(customer_id, first_name, last_name) VALUES(1, 'Fedrik', 'Pedro')");

            }catch (SQLException sqlException){
                connection.rollback(savepoint1);

            }

            connection.commit();
            connection.close();

        }


    public static List<TransactionWithCustomerId> showTransactionWhereId(Long transaction_id) throws SQLException {
        List<TransactionWithCustomerId> transactionWithCustomerIdList = new ArrayList<>();

        Connection connection = DbConfig.getConnection();
        PreparedStatement preparedStatement;

        preparedStatement = connection.prepareStatement("SELECT * FROM transactions WHERE transaction_id = ?");
        preparedStatement.setInt(1, 1);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            TransactionWithCustomerId transactionWithCustomerId = new TransactionWithCustomerId();
            transactionWithCustomerId.setTransaction_id(resultSet.getLong("transaction_id"));
            transactionWithCustomerId.setAmount(resultSet.getDouble("amount"));
            transactionWithCustomerId.setCustomer_id(resultSet.getLong("customer_id"));

            transactionWithCustomerIdList.add(transactionWithCustomerId);

        }
        connection.close();

        return transactionWithCustomerIdList;
    }


    public static List<Customer> showACustomerWhereTheFirstName(String first_name) throws SQLException {
        List<Customer> customerList = new ArrayList<>();
        Connection connection = DbConfig.getConnection();

        // Create a statement object.
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM customers WHERE first_name = ?");

        // Set the SQL statement parameters.
        preparedStatement.setString(1, first_name);

        // Execute the SQL statement.
        ResultSet rs = preparedStatement.executeQuery();

        // Process the results of the SQL statement.
        while (rs.next()) {
            Customer customer = new Customer();
            customer.setCustomer_id(rs.getLong("customer_id"));
            customer.setFirst_name(rs.getString("first_name"));
            customer.setLast_name(rs.getString("last_name"));

            customerList.add(customer);

        }

        // Close the statement and connection objects.
        preparedStatement.close();
        connection.close();

        return customerList;
    }


    public static List<TransactionWithCustomer> showCustomersWithTheirTransactions() {
        List<TransactionWithCustomer> transactionWithCustomerList = new ArrayList<>();
        Connection connection = DbConfig.getConnection();
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.execute("SELECT transaction_id, amount, first_name, last_name FROM transactions " +
                    "AS t INNER JOIN customers AS c ON t.customer_id = c.customer_id;");


            ResultSet resultSet2 = statement.getResultSet();
            while (resultSet2.next()) {
                Customer customer = new Customer();
                TransactionWithCustomer customerTransaction = new TransactionWithCustomer();

                customer.setFirst_name(
                        resultSet2.getString("first_name"));
                customer.setLast_name(
                        resultSet2.getString("last_name"));

                customerTransaction.setTransaction_id(
                        resultSet2.getLong("transaction_id"));
                customerTransaction.setAmount(
                        resultSet2.getDouble("amount"));

                customerTransaction.setCustomer(customer);
                transactionWithCustomerList.add(customerTransaction);

            }
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }

        return transactionWithCustomerList;
    }


    public static List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        Connection connection = DbConfig.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customers");


            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomer_id(
                        resultSet.getLong("customer_id"));
                customer.setFirst_name(
                        resultSet.getString("first_name"));
                customer.setLast_name(
                        resultSet.getString("last_name"));
                customers.add(customer);

            }
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }

        return customers;
    }
}
