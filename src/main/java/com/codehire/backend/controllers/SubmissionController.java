package com.codehire.backend.controllers;

import com.codehire.backend.dto.SubmissionRequest;
import com.codehire.backend.engine.JudgeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/submissions")
@CrossOrigin(origins = "http://localhost:4200")
public class SubmissionController {

    @PostMapping("/run")
    public String runCode(@RequestBody SubmissionRequest request) {
        System.out.println("\n[SERVER] HTTP POST request intercepted at /api/v1/submissions/run");
        System.out.println("[SERVER] Compiling/Running Problem ID: " + request.getProblemId());

        String verdict = JudgeService.submitCode(
                request.getProblemId(),
                request.getLanguage(),
                request.getCode()
        );

        return verdict;
    }
}