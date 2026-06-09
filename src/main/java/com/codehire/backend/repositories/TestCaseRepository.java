package com.codehire.backend.repositories;

import com.codehire.backend.models.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Integer> {

    // Spring sees this method name and automatically generates:
    // SELECT * FROM test_cases WHERE problem_id = ? ORDER BY id ASC
    List<TestCase> findByProblemIdOrderByIdAsc(int problemId);
}