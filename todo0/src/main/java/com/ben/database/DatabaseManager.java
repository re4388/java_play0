package com.ben.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static java.sql.DriverManager.getConnection;

public class DatabaseManager {
    private static final String SQLITE_URL = "jdbc:sqlite:todo.db";

    static {
        try(Connection connection = getConnection();
            Statement stmt = connection.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS todos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    done INTEGER DEFAULT 0
                )
            """);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(SQLITE_URL);
        } catch (Exception e) {
            throw new RuntimeException("connection fail", e);
        }
    }
}
