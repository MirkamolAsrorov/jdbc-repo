package org.example.service;

import javax.sql.RowSet;
import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.*;
import javax.sql.rowset.spi.SyncProvider;
import javax.sql.rowset.spi.SyncProviderException;
import javax.swing.plaf.PanelUI;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

public class DbConfig {
    public static final String URL = "jdbc:postgresql://localhost:5432/test";
    public static final String USER = "postgres";
    public static final String PASSWORD = "root123";

    public static Connection getConnection() {
        Connection connection;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }

    public static JdbcRowSet configureJDBCRowSet(){
        try {
            RowSetFactory rowSetFactory = RowSetProvider.newFactory();
            JdbcRowSet jdbcRS = rowSetFactory.createJdbcRowSet();
            jdbcRS.setUrl(URL);
            jdbcRS.setUsername(USER);
            jdbcRS.setPassword(PASSWORD);


            return jdbcRS;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static CachedRowSet configureCachedRowSet(){
        try {
            CachedRowSet cachedRS = RowSetProvider.newFactory().createCachedRowSet();
            cachedRS.setUrl(URL);
            cachedRS.setUsername(USER);
            cachedRS.setPassword(PASSWORD);

            return cachedRS;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
