package com.scholarship.backend.repositories;

import com.scholarship.backend.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByUser_UserId(long userId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Profile p WHERE p.user.userId = :userId")
    void deleteByUser_UserId(long userId);
}