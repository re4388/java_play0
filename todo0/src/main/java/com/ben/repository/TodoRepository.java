package com.ben.repository;



import com.ben.database.DatabaseManager;
import com.ben.model.Todo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TodoRepository {

    public TodoRepository() {
    }

    public void add(String title) {
        String sql = "INSERT INTO todos(title, done) VALUES(?, 0)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Todo> findAll() {
        String sql = "SELECT id, title, done FROM todos";

        List<Todo> list = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {

            while (resultSet.next()) {
                list.add(new Todo(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getInt("done") == 1
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void toggleDone(int id, boolean done) {

        String sql = "UPDATE todos SET done=? WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, done ? 1 : 0);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
