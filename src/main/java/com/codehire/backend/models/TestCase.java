package com.codehire.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "test_cases")
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "problem_id")
    private int problemId;

    @Column(name = "input_data")
    private String inputData;

    @Column(name = "expected_output")
    private String expectedOutput;

    @Column(name = "is_hidden")
    private boolean isHidden;

    // 1. Default constructor is absolutely required by JPA
    public TestCase() {}

    // 2. Fully loaded constructor for your own logic
    public TestCase(int id, int problemId, String inputData, String expectedOutput, boolean isHidden) {
        this.id = id;
        this.problemId = problemId;
        this.inputData = inputData;
        this.expectedOutput = expectedOutput;
        this.isHidden = isHidden;
    }

    // 3. Proper Getters for Spring and Jackson to read the data
    public int getId() { return id; }
    public int getProblemId() { return problemId; }
    public String getInputData() { return inputData; }
    public String getExpectedOutput() { return expectedOutput; }
    public boolean isHidden() { return isHidden; }
}