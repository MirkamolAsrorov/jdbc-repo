# learning-jdbc
This repo was created to learn how to use JDBC correctly.
## GUIDES TO CONNECT JAVA TO OUR DATABASE.

### 1) ADD postgresql dependency into you maven;
        <!-- https://mvnrepository.com/artifact/org.postgresql/postgresql -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.6.0</version>
        </dependency>
### 2) TO CONNCET DATA BASE, we write:
Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/database", "postgres", "password");

### 3) TO WRITE A QUERY INTO OUR DATA BASE, we call preparedStatement function from above repo:
PreparedStatement preparedStatement =conn.prepareStatement("SELECT transaction_id, amount, first_name, last_name FROM transactions AS t" +
                "INNER JOIN customers AS c ON t.customer_id = c.customer_id;");
### 4) TO EXECUTE A QUERY:
ResultSet resultSet2 = preparedStatement.executeQuery();

### 5) TO ITTERATE THE EXECUTED QUERY
while (resultSet2.next()) {
            System.out.print(resultSet2.getString("transaction_id") + "\t");
            System.out.print(resultSet2.getString("amount") + "\t");
            System.out.print(resultSet2.getString("first_name") + "\t");
            System.out.print(resultSet2.getString("last_name") + "\t");
        }
### 6) TO CLOSE connection and preparedStatement;

        preparedStatement.close();
        conn.close();

              

