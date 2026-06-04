package com.codehire.backend.database;

import com.codehire.backend.models.TestCase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProblemDAO {

    // Fetch all test cases for a given problem ID
    public static List<TestCase> getTestCasesForProblem(int problemId) {
        List<TestCase> testCases = new ArrayList<>();
        String query = "SELECT * FROM test_cases WHERE problem_id = ? ORDER BY id ASC";

        try {
            // 1. Force the JVM to load the PostgreSQL translation engine into RAM
            Class.forName("org.postgresql.Driver");

            // 2. Now attempt the connection using your existing try-with-resources
            try (Connection conn = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, problemId);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    testCases.add(new TestCase(
                            rs.getInt("id"),
                            rs.getInt("problem_id"),
                            rs.getString("input_data"),
                            rs.getString("expected_output"),
                            rs.getBoolean("is_hidden")
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Database Error: " + e.getMessage());
            e.printStackTrace(); // Added this so you can see the exact line it fails on if it happens again
        }

        return testCases;
    }
}