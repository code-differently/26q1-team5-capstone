package com.scholarship.backend.repositories;

import com.scholarship.backend.entities.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScholarshipRepository extends JpaRepository<Scholarship, Long> {
    List<Scholarship> findByFieldOfStudy(String field);
    List<Scholarship> findByState(String state);
    List<Scholarship> findByDeadlineGreaterThan(LocalDate date);
    boolean existsByNameAndSourceApi(String name, String sourceApi);
    Scholarship findByNameAndSourceApi(String name, String sourceApi);
}
