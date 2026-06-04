package com.codehire.backend.dto;

public class SubmissionRequest {
    private int problemId;
    private String language;
    private String code;

    // Getters and Setters: Spring Boot secretly uses these to inject the JSON data
    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}