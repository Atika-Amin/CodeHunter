package com.example.demo;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/CodeHunter?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Mdhamim07";

    private static Connection conn;
    private static final int DEFAULT_LEVEL = 1;

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Database connected successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("❌ JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    // Create a new user_progress row
    public static void createUserProgress(int userId) {
        String sql = "INSERT INTO user_progress (user_id, max_level, knowledge_level, points) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, 0);     // max_level
            stmt.setInt(3, 0);     // knowledge_level
            stmt.setInt(4, 0);     // points
            stmt.executeUpdate();
            System.out.println("✅ New user progress created for userId: " + userId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get max level
    public static int getUserMaxLevel(int userId) {
        String sql = "SELECT max_level FROM user_progress WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("max_level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Update max level
    public static void updateUserMaxLevel(int userId, int newLevel) {
        String sql = "UPDATE user_progress SET max_level = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newLevel);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            System.out.println("✅ User's level updated to " + newLevel);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update knowledge level only
    public static void updateKnowledgeLevel(int userId, int newLevel) {
        // Ensure progress exists
        String checkQuery = "SELECT * FROM user_progress WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

            checkStmt.setInt(1, userId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                // If not found, create progress first
                createUserProgress(userId);
            }

            // Now update
            String updateQuery = "UPDATE user_progress SET knowledge_level = ? WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setInt(1, newLevel);
                stmt.setInt(2, userId);
                stmt.executeUpdate();
                System.out.println("✅ Knowledge level updated for userId: " + userId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static int getKnowledgeLevel(int userId) {
        String sql = "SELECT knowledge_level FROM user_progress WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("knowledge_level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static void updatePoints(int userId, int newPoints) {
        String checkQuery = "SELECT * FROM user_progress WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

            checkStmt.setInt(1, userId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                createUserProgress(userId);
            }

            String updateQuery = "UPDATE user_progress SET points = ? WHERE user_id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setInt(1, newPoints);
                updateStmt.setInt(2, userId);
                updateStmt.executeUpdate();
                System.out.println("✅ Points updated to " + newPoints + " for user ID: " + userId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getPoints(int userId) {
        String sql = "SELECT points FROM user_progress WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("points");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ✅ Update knowledge_level, points, and max_level after quiz
    public static void updateProgressAfterQuiz(int userId, int levelIncrease, int pointsIncrease) {
        String selectQuery = "SELECT knowledge_level, points, max_level FROM user_progress WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {

            selectStmt.setInt(1, userId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int currentKnowledge = rs.getInt("knowledge_level");
                int currentPoints = rs.getInt("points");
                int currentMaxLevel = rs.getInt("max_level");

                int newKnowledge = currentKnowledge + levelIncrease;
                int newPoints = currentPoints + pointsIncrease;
                int newMaxLevel = Math.max(currentMaxLevel, newKnowledge); // Optional logic

                String updateQuery = "UPDATE user_progress SET knowledge_level = ?, points = ?, max_level = ? WHERE user_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, newKnowledge);
                updateStmt.setInt(2, newPoints);
                updateStmt.setInt(3, newMaxLevel);
                updateStmt.setInt(4, userId);
                updateStmt.executeUpdate();

                System.out.println("✅ Progress updated: knowledge=" + newKnowledge + ", points=" + newPoints + ", max_level=" + newMaxLevel);
            } else {
                createUserProgress(userId); // Retry if not found
                updateProgressAfterQuiz(userId, levelIncrease, pointsIncrease);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
