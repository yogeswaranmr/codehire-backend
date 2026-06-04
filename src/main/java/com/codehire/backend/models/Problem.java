package com.codehire.backend.models;

public class Problem {
    public int id;
    public String title;
    public String description;
    public String difficulty;
    public String functionName;
    public String inputParams;
    public String constraints;

    public Problem(int id, String title, String description, String difficulty, String functionName, String inputParams, String constraints) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.functionName = functionName;
        this.inputParams = inputParams;
        this.constraints = constraints;
    }
}
