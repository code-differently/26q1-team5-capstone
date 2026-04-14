package com.scholarship.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long profileId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column
    private Double gpa;

    @Column
    private String major;

    @Column
    private String enrollmentStatus;

    @Column
    private Boolean needsFinancialAid;

    @Column
    private String state;

    @Column
    private String ethnicity;

    @Column(columnDefinition = "TEXT")
    private String careerGoals;

    @Column(columnDefinition = "TEXT")
    private String interests;

    public Profile() {}

    public Profile(User user, String name) {
        this.user = user;
        this.name = name;
    }

    // Getters
    public long getProfileId() {
        return profileId;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public Double getGpa() {
        return gpa;
    }

    public String getMajor() {
        return major;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public Boolean getNeedsFinancialAid() {
        return needsFinancialAid;
    }

    public String getState() {
        return state;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public String getCareerGoals() {
        return careerGoals;
    }

    public String getInterests() {
        return interests;
    }

    // Setters
    public void setUser(User user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public void setNeedsFinancialAid(Boolean needsFinancialAid) {
        this.needsFinancialAid = needsFinancialAid;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public void setCareerGoals(String careerGoals) {
        this.careerGoals = careerGoals;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }
}
