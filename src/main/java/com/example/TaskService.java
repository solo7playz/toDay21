package com.example;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TaskService {

    private static final Properties dbProps = new Properties();

    static {
        try (InputStream is = TaskService.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                dbProps.load(is);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load db.properties", e);
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load PostgreSQL driver", e);
        }
    }

    private Connection getConnection() throws SQLException {
        String url = dbProps.getProperty("db.url", "jdbc:postgresql://localhost:5432/postgres");
        String user = dbProps.getProperty("db.user", "postgres");
        String password = dbProps.getProperty("db.password", "root");
        return DriverManager.getConnection(url, user, password);
    }

    public void ensureTableExists() {
        String sql = "DROP TABLE IF EXISTS \"Tasks\"; " +
                     "CREATE TABLE \"Tasks\" (" +
                     "id SERIAL PRIMARY KEY, " +
                     "name VARCHAR(255) NOT NULL, " +
                     "info TEXT, " +
                     "done BOOLEAN DEFAULT FALSE)";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Warning: could not create table: " + e.getMessage());
        }
    }

    public void addTask(Task task) {
        String sql = "INSERT INTO tasks (name, info, done) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getName());
            stmt.setString(2, task.getInfo() != null ? task.getInfo() : "");
            stmt.setBoolean(3, task.isDone());
            stmt.executeUpdate();

            try (Statement seq = conn.createStatement();
                 ResultSet rs = seq.executeQuery("SELECT currval(\"Tasks_id_seq\")")) {
                if (rs.next()) {
                    task.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding task", e);
        }
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, name, info, done FROM \"Tasks\" ORDER BY id DESC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Task task = new Task(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("info"),
                        rs.getBoolean("done")
                );
                tasks.add(task);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching tasks", e);
        }
        return tasks;
    }

    public void updateTask(Task task) {
        String sql = "UPDATE \"Tasks\" SET name = ?, info = ?, done = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getName());
            stmt.setString(2, task.getInfo());
            stmt.setBoolean(3, task.isDone());
            stmt.setInt(4, task.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating task", e);
        }
    }

    public void deleteTask(int id) {
        String sql = "DELETE FROM \"Tasks\" WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting task", e);
        }
    }
}
