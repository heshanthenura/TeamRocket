package com.heshanthenura.teamrocket.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DBConnection {

    private static final String DB_URL = "jdbc:h2:" + System.getProperty("user.dir") + File.separator + "data.db";
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";
    static Logger logger = Logger.getLogger("logger");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createPersonTable() {
        try (Connection connection = getConnection()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS person ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(255), "
                    + "netWorth VARCHAR(255), "
                    + "age INT, "
                    + "countryOrTerritory VARCHAR(255), "
                    + "source VARCHAR(255), "
                    + "industry VARCHAR(255))";

            try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
                preparedStatement.executeUpdate();
                logger.info("Table 'person' created or already exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createPersonTable();
    }
}
