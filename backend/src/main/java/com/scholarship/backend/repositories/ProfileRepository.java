package com.scholarship.backend.repositories;

import com.scholarship.backend.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByUser_UserId(long userId);
    void deleteByUser_UserId(long userId);
}
