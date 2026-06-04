package com.codehire.backend.models;

public class TestCase {
    public int id;
    public int problemId;
    public String inputData;
    public String expectedOutput;
    public boolean isHidden;

    public TestCase(int id, int problemId, String inputData, String expectedOutput, boolean isHidden) {
        this.id = id;
        this.problemId = problemId;
        this.inputData = inputData;
        this.expectedOutput = expectedOutput;
        this.isHidden = isHidden;
    }
}