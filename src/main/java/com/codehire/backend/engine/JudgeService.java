package com.codehire.backend.engine;

import com.codehire.backend.models.TestCase;
import com.codehire.backend.repositories.TestCaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Tells Spring to take control of this class
public class JudgeService {

    private final TestCaseRepository testCaseRepository;

    // Spring automatically injects the database repository here
    public JudgeService(TestCaseRepository testCaseRepository) {
        this.testCaseRepository = testCaseRepository;
    }

    public String submitCode(int problemId, String language, String candidateMethodCode) {

        List<TestCase> testCases = testCaseRepository.findByProblemIdOrderByIdAsc(problemId);
        if (testCases.isEmpty()) return "❌ System Error: No test cases found.";

        for (int i = 0; i < testCases.size(); i++) {
            TestCase tc = testCases.get(i);

            // 1. CHOOSE THE WRAPPER BASED ON LANGUAGE
            String fullCode = "";
            if (language.equalsIgnoreCase("java")) {
                fullCode = generateJavaWrapper(candidateMethodCode, tc.getInputData());
            } else if (language.equalsIgnoreCase("python")) {
                fullCode = generatePythonWrapper(candidateMethodCode, tc.getInputData());
            } else {
                return "❌ Unsupported Language: " + language;
            }

            try {
                // 2. PASS THE LANGUAGE TO THE SANDBOX
                String actualOutput = com.codehire.backend.engine.CodeExecutor.execute(fullCode, language);

                // 3. GRADE THE OUTPUT
                if (actualOutput.startsWith("❌")) {
                    return "Runtime Error on Test Case " + (i + 1) + ":\n" + actualOutput;
                }

                if (!actualOutput.equals(tc.getExpectedOutput())) {
                    // 1. If it's hidden, give them absolutely nothing to work with.
                    if (tc.isHidden()) {
                        return "❌ Wrong Answer on Test Case " + (i + 1) + " (Hidden)\n";
                    }

                    // 2. If it's visible, give them the full debugging breakdown.
                    return "❌ Wrong Answer on Test Case " + (i + 1) + "\n" +
                            "Input: " + tc.getInputData() + "\n" +
                            "Expected: " + tc.getExpectedOutput() + "\n" +
                            "Actual: " + actualOutput;
                }

            } catch (Exception e) {
                return "❌ System Error during execution.";
            }
        }
        return "✅ ACCEPTED! All test cases passed.";
    }

    private static String generateJavaWrapper(String candidateMethod, String inputData) {
        return
                "public class Solution {\n" +
                        "    " + candidateMethod + "\n" +
                        "    public static void main(String[] args) {\n" +
                        "        Solution sol = new Solution();\n" +
                        "        System.out.print(sol.solve(\"" + inputData + "\"));\n" +
                        "    }\n" +
                        "}";
    }

    private static String generatePythonWrapper(String candidateMethod, String inputData) {
        // Python doesn't need classes, just the method and a print statement at the bottom
        return
                candidateMethod + "\n\n" +
                        "if __name__ == '__main__':\n" +
                        "    # We inject the DB input into the method call\n" +
                        "    result = solve('" + inputData + "')\n" +
                        "    print(result, end='')\n"; // end='' prevents Python from printing a hidden newline!
    }
}