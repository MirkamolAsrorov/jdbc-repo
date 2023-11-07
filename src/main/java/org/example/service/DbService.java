package org.example.service;

import org.example.model.Customer;
import org.example.model.TransactionWithCustomer;
import org.example.model.TransactionWithCustomerId;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DbService {
    public static void retrieveImageFromDatabaseToComputer(String imagePath){
        Connection connection = DbConfig.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM image");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                byte[] bytes = resultSet.getBytes("image_data");
                int bLength = bytes.length;

                FileOutputStream fileOutputStream = new FileOutputStream(imagePath);
                fileOutputStream.write(bytes,0, bLength);

                fileOutputStream.flush();
                fileOutputStream.close();
            }
            System.out.println("Look at the " + imagePath);

            preparedStatement.close();
            connection.close();

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static int storeImageInDatabase(String imagePath){
        String sql = "INSERT INTO image(image_name, data) VALUES(?,?)";

        Connection connection = DbConfig.getConnection();
        File file = new File(imagePath);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, file.getName());
            preparedStatement.setBinaryStream(2, fileInputStream, file.length());
            int i = preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
            return i;

        } catch (FileNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static void showAllViews(){
        Connection connection = DbConfig.getConnection();

        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String[] views = {"VIEW"};
            ResultSet resultSet = databaseMetaData.getTables(null, null, null, views);
            while (resultSet.next()) {
                System.out.println(resultSet.getString(3));

            }
            connection.close();

        }catch (SQLException ex){
            throw new RuntimeException(ex);
        }


    }


    public static void showAllTables() {
        Connection connection = DbConfig.getConnection();
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String[] table = {"TABLE"};

            ResultSet tables = databaseMetaData.getTables(null, null, null, table);


            while (tables.next()) {
                System.out.println(tables.getString(3));

            }
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void getDetailsAboutDatabase() {
        Connection connection = DbConfig.getConnection();

        try {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            System.out.println("Driver Name: " + dbMetaData.getDriverName());
            System.out.println("Driver Version: " + dbMetaData.getDriverVersion());
            System.out.println("Username: " + dbMetaData.getUserName());
            System.out.println("Database product Name: " + dbMetaData.getDatabaseProductName());
            System.out.println("Database product Ver: " + dbMetaData.getDatabaseProductVersion());

            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }


    public static void getInformationAboutTheColumn() {
        Connection connection = DbConfig.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customers");
            ResultSetMetaData metaData = resultSet.getMetaData();

            displayMetaData(metaData, 1);
            System.out.println();

            displayMetaData(metaData, 1);
            System.out.println();

            displayMetaData(metaData, 1);
            System.out.println();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void displayMetaData(ResultSetMetaData metaData, int columnIndex) {
        try {
            System.out.println("Total columns: " + metaData.getColumnCount());
            System.out.println("Column Name of 1st column: " + metaData.getColumnName(columnIndex));
            System.out.println("Column Type Name of 1st column: " + metaData.getColumnType(columnIndex));
            System.out.println("Column Type of 1st column: " + metaData.getColumnType(columnIndex));

        } catch (SQLException ex) {
            throw new RuntimeException(ex);

        }
    }

    public static void updateSecondRow() {
        String sql = "SELECT * FROM customers";

        Connection connection = DbConfig.getConnection();
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.absolute(2)) {
                // GO directly 2 row;
                display(resultSet);

                resultSet.updateString("first_name", "WellMan");
                resultSet.updateRow();

            }
            resultSet.beforeFirst();
            System.out.println("All the rows Table =>");
            while (resultSet.next()) {
                display(resultSet);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void navigateData() {
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


    public static void display(ResultSet resultSet) {

        try {
            System.out.print("customer_id " + resultSet.getLong(1));
            System.out.print(" firstName " + resultSet.getString(2));
            System.out.println(" lastName " + resultSet.getString(3));
            System.out.println();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }


    public static void batchThreeOperations(String sql, String sql2, String sql3) {
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

        } catch (SQLException sqlException) {
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
