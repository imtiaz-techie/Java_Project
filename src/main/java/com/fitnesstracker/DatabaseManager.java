package com.fitnesstracker;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static DatabaseManager instance;
    private static final String DB_URL = "jdbc:sqlite:fitness_tracker.db";
    private Connection connection;

    private DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void initializeDatabase() {
        try (Statement statement = connection.createStatement()) {
            // Create workouts table
            statement.execute("""
                CREATE TABLE IF NOT EXISTS workouts (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    type TEXT NOT NULL,
                    duration INTEGER NOT NULL,
                    intensity TEXT NOT NULL,
                    calories_burned INTEGER NOT NULL,
                    date TEXT NOT NULL
                )
            """);

            // Create goals table
            statement.execute("""
                CREATE TABLE IF NOT EXISTS goals (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    description TEXT NOT NULL,
                    target_value INTEGER NOT NULL,
                    deadline TEXT NOT NULL,
                    completed BOOLEAN DEFAULT FALSE
                )
            """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addWorkout(Workout workout) {
        String sql = "INSERT INTO workouts (type, duration, intensity, calories_burned, date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, workout.getType());
            pstmt.setInt(2, workout.getDuration());
            pstmt.setString(3, workout.getIntensity());
            pstmt.setInt(4, workout.getCaloriesBurned());
            pstmt.setString(5, workout.getDate().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Workout> getWorkouts() {
        List<Workout> workouts = new ArrayList<>();
        String sql = "SELECT * FROM workouts ORDER BY date DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Workout workout = new Workout(
                    rs.getString("type"),
                    rs.getInt("duration"),
                    rs.getString("intensity"),
                    rs.getInt("calories_burned"),
                    LocalDate.parse(rs.getString("date"))
                );
                workout.setId(rs.getInt("id"));
                workouts.add(workout);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workouts;
    }

    public void addGoal(Goal goal) {
        String sql = "INSERT INTO goals (description, target_value, deadline, completed) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, goal.getDescription());
            pstmt.setInt(2, goal.getTargetValue());
            pstmt.setString(3, goal.getDeadline().toString());
            pstmt.setBoolean(4, goal.isCompleted());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Goal> getGoals() {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goals ORDER BY deadline ASC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Goal goal = new Goal(
                    rs.getString("description"),
                    rs.getInt("target_value"),
                    LocalDate.parse(rs.getString("deadline"))
                );
                goal.setId(rs.getInt("id"));
                goal.setCompleted(rs.getBoolean("completed"));
                goals.add(goal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return goals;
    }

    public void updateGoal(Goal goal) {
        String sql = "UPDATE goals SET completed = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, goal.isCompleted());
            pstmt.setInt(2, goal.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 