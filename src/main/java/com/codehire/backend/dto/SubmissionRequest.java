package com.codehire.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class SubmissionRequest {

    @Min(value = 1, message = "Problem ID must be valid and greater then 0.")
    private int problemId;

    @NotBlank(message = "Language cannot be blank.")
    private String language;

    @NotBlank(message= "Cde cannot be blank.")
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