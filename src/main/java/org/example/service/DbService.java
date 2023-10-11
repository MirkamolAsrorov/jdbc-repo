package org.example.service;

import org.example.model.Customer;
import org.example.model.CustomerTransactions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class DbService {

    public static List<CustomerTransactions> showCustomersWithTheirTransactions() {
        List<CustomerTransactions> customerTransactionsList = new ArrayList<>();
        Connection connection = DbConfig.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute("SELECT transaction_id, amount, first_name, last_name FROM transactions " +
                    "AS t INNER JOIN customers AS c ON t.customer_id = c.customer_id;");


            ResultSet resultSet2 = statement.getResultSet();
            while (resultSet2.next()) {
                Customer customer = new Customer();
                CustomerTransactions customerTransaction = new CustomerTransactions();

                customer.setFirst_name(
                        resultSet2.getString("first_name"));
                customer.setLast_name(
                        resultSet2.getString("last_name"));

                customerTransaction.setTransaction_id(
                        resultSet2.getInt("transaction_id"));
                customerTransaction.setAmount(
                        resultSet2.getDouble("amount"));

                customerTransaction.setCustomer(customer);
                customerTransactionsList.add(customerTransaction);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customerTransactionsList;
    }


    public static List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        Connection connection = DbConfig.getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM customers");

            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomer_id(
                        resultSet.getString("customer_id"));
                customer.setFirst_name(
                        resultSet.getString("first_name"));
                customer.setLast_name(
                        resultSet.getString("last_name"));
                customers.add(customer);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customers;
    }
}
