package com.scholarship.backend.repositories;

import com.scholarship.backend.entities.Application;
import com.scholarship.backend.entities.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUser_UserId(long userId);
    List<Application> findByStatus(ApplicationStatus status);
    List<Application> findByUser_UserIdAndStatus(long userId, ApplicationStatus status);
}