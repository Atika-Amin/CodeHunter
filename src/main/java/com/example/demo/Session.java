package com.example.demo;

public class Session {
    private static int currentUserId;
    private static int knowledgeLevel;
    private static int points;

    public static void setCurrentUserId(int userId) {
        currentUserId = userId;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static int getKnowledgeLevel() {
        return knowledgeLevel;
    }

    public static void setKnowledgeLevel(int level) {
        knowledgeLevel = level;
    }

    public static int getPoints() {
        return points;
    }

    public static void setPoints(int p) {
        points = p;
    }
}

