package com.example.demo;
public class Challenge {
    private String question;
    private String[] expectedKeywords; // keywords to check in the answer

    public Challenge(String question, String[] expectedKeywords) {
        this.question = question;
        this.expectedKeywords = expectedKeywords;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getExpectedKeywords() {
        return expectedKeywords;
    }
}

