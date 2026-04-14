package com.scholarship.backend.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationStatusTest {

    @Test
    void testApplicationStatusValues() {
        assertNotNull(ApplicationStatus.SAVED);
        assertNotNull(ApplicationStatus.IN_PROGRESS);
        assertNotNull(ApplicationStatus.SUBMITTED);
        assertNotNull(ApplicationStatus.AWARDED);
        assertNotNull(ApplicationStatus.REJECTED);
    }

    @Test
    void testApplicationStatusEnum() {
        ApplicationStatus[] statuses = ApplicationStatus.values();
        assertEquals(5, statuses.length);
    }

    @Test
    void testApplicationStatusValueOf() {
        assertEquals(ApplicationStatus.SAVED, ApplicationStatus.valueOf("SAVED"));
        assertEquals(ApplicationStatus.IN_PROGRESS, ApplicationStatus.valueOf("IN_PROGRESS"));
        assertEquals(ApplicationStatus.SUBMITTED, ApplicationStatus.valueOf("SUBMITTED"));
        assertEquals(ApplicationStatus.AWARDED, ApplicationStatus.valueOf("AWARDED"));
        assertEquals(ApplicationStatus.REJECTED, ApplicationStatus.valueOf("REJECTED"));
    }

    @Test
    void testApplicationStatusOrdinal() {
        assertEquals(0, ApplicationStatus.SAVED.ordinal());
        assertEquals(1, ApplicationStatus.IN_PROGRESS.ordinal());
        assertEquals(2, ApplicationStatus.SUBMITTED.ordinal());
        assertEquals(3, ApplicationStatus.AWARDED.ordinal());
        assertEquals(4, ApplicationStatus.REJECTED.ordinal());
    }
}

