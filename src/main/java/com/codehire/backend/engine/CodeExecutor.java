package com.codehire.backend.engine;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CodeExecutor {

    public static String execute(String fullCode, String language) {
        String executionId = UUID.randomUUID().toString(); // generate an unique ID for given fullCode
        Path workDir = Paths.get("temp_exec", executionId); // glues ID to dir and returns as path object -> eg, temp_exec/1234

        try {
            Files.createDirectories(workDir); // creates an actual directory for given Path obj
            ProcessBuilder pb; // creating pb object which can launch os-level processes

            // --- LANGUAGE ROUTING LOGIC ---
            if (language.equalsIgnoreCase("java")) {
                Path sourceFile = workDir.resolve("Solution.java"); // append given string to workDir and returns new Path obj
                Files.writeString(sourceFile, fullCode); // writes fullCode to sourceFile

                // 1. Compile the Java file
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler(); // returns javac.exe (compiler) ref as java obj
                int compilationResult = compiler.run(null, null, null, sourceFile.toString()); // compiles the given code and returns result
                if (compilationResult != 0) {
                    return "❌ Compilation Error: Please check your syntax.";
                }

                // 2. Prepare the Java execution command
                pb = new ProcessBuilder("java", "-Xmx256m", "-cp", ".", "Solution"); // puts all the strings to construct command, here we made command to run java with max ram 256mb, classpath as current dir and main class as Solution

            } else if (language.equalsIgnoreCase("python")) { // same as java block but without compilation
                Path sourceFile = workDir.resolve("Solution.py");
                Files.writeString(sourceFile, fullCode);

                // 1. No compilation needed! Just prepare the Python execution command.
                pb = new ProcessBuilder("python", "Solution.py");

            } else {
                return "❌ System Error: Unsupported Language";
            }

            // --- COMMON EXECUTION LOGIC ---
            pb.directory(workDir.toFile()); // Set the working directory (now we are here)
            Process process = pb.start(); // Actually start the process with the given command and working directory

            // The 2-Second Alarm Clock
            boolean finishedInTime = process.waitFor(2, TimeUnit.SECONDS);

            if (!finishedInTime) {
                process.destroyForcibly();
                return "❌ Time Limit Exceeded (TLE): Code took longer than 2.0 seconds.";
            }

            // Capture outputs
            String output = new String(process.getInputStream().readAllBytes());
            String error = new String(process.getErrorStream().readAllBytes());

            if (!error.isEmpty()) {
                if (error.contains("java.lang.OutOfMemoryError")) {
                    return "❌ Memory Limit Exceeded (MLE): Code attempted to use more than 256MB of RAM.";
                }
                return "❌ Runtime Error:\n" + error.trim();
            }

            return output.trim();

        } catch (Exception e) {
            return "❌ System Execution Failed: " + e.getMessage();
        } finally {
            deleteDirectory(workDir.toFile()); // Always clean up
        }
    }

    private static void deleteDirectory(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) file.delete();
            }
            dir.delete();
        }
    }
}