package com.scholarship.backend.repositories;

import com.scholarship.backend.entities.Scholarship;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ScholarshipRepositoryTest {

    @Autowired
    private ScholarshipRepository scholarshipRepository;

    @Test
    void testFindByFieldOfStudy() {
        // Create scholarships with different fields
        Scholarship csScholarship = new Scholarship("CS Award", 5000.0, LocalDate.of(2026, 6, 30));
        csScholarship.setFieldOfStudy("Computer Science");
        scholarshipRepository.save(csScholarship);

        Scholarship engScholarship = new Scholarship("Engineering Grant", 7500.0, LocalDate.of(2026, 7, 15));
        engScholarship.setFieldOfStudy("Engineering");
        scholarshipRepository.save(engScholarship);

        Scholarship anotherCs = new Scholarship("Tech Scholarship", 3000.0, LocalDate.of(2026, 8, 1));
        anotherCs.setFieldOfStudy("Computer Science");
        scholarshipRepository.save(anotherCs);

        // Test finding by field of study
        List<Scholarship> csScholarships = scholarshipRepository.findByFieldOfStudy("Computer Science");
        assertEquals(2, csScholarships.size());

        List<Scholarship> engScholarships = scholarshipRepository.findByFieldOfStudy("Engineering");
        assertEquals(1, engScholarships.size());
        assertEquals("Engineering Grant", engScholarships.get(0).getName());
    }

    @Test
    void testFindByState() {
        Scholarship caScholarship = new Scholarship("CA Award", 4000.0, LocalDate.of(2026, 6, 30));
        caScholarship.setState("California");
        scholarshipRepository.save(caScholarship);

        Scholarship txScholarship = new Scholarship("TX Grant", 6000.0, LocalDate.of(2026, 7, 15));
        txScholarship.setState("Texas");
        scholarshipRepository.save(txScholarship);

        Scholarship anotherCa = new Scholarship("Golden State Fund", 3500.0, LocalDate.of(2026, 8, 1));
        anotherCa.setState("California");
        scholarshipRepository.save(anotherCa);

        // Test finding by state
        List<Scholarship> caScholarships = scholarshipRepository.findByState("California");
        assertEquals(2, caScholarships.size());

        List<Scholarship> txScholarships = scholarshipRepository.findByState("Texas");
        assertEquals(1, txScholarships.size());
        assertEquals("TX Grant", txScholarships.get(0).getName());
    }

    @Test
    void testFindByDeadlineGreaterThan() {
        LocalDate now = LocalDate.of(2026, 4, 14);

        Scholarship pastDeadline = new Scholarship("Expired", 2000.0, LocalDate.of(2026, 4, 10));
        scholarshipRepository.save(pastDeadline);

        Scholarship upcoming1 = new Scholarship("Upcoming 1", 3000.0, LocalDate.of(2026, 4, 20));
        scholarshipRepository.save(upcoming1);

        Scholarship upcoming2 = new Scholarship("Upcoming 2", 4000.0, LocalDate.of(2026, 5, 1));
        scholarshipRepository.save(upcoming2);

        Scholarship farFuture = new Scholarship("Far Future", 5000.0, LocalDate.of(2026, 12, 31));
        scholarshipRepository.save(farFuture);

        // Test finding scholarships with deadline after given date
        List<Scholarship> futureScholarships = scholarshipRepository.findByDeadlineGreaterThan(now);
        assertEquals(3, futureScholarships.size());

        // Should not include the expired one
        boolean hasExpired = futureScholarships.stream()
                .anyMatch(s -> s.getName().equals("Expired"));
        assertFalse(hasExpired);
    }

    @Test
    void testSaveAndFindById() {
        Scholarship scholarship = new Scholarship("Test Scholarship", 2500.0, LocalDate.of(2026, 9, 1));
        scholarship.setDescription("A test scholarship");
        scholarship.setApplicationUrl("https://test.edu");
        scholarship.setEligibilityCriteria("GPA > 3.0");
        scholarship = scholarshipRepository.save(scholarship);

        assertNotNull(scholarship.getScholarshipId());

        Scholarship found = scholarshipRepository.findById(scholarship.getScholarshipId()).orElse(null);
        assertNotNull(found);
        assertEquals("Test Scholarship", found.getName());
        assertEquals(2500.0, found.getAmount());
        assertEquals("A test scholarship", found.getDescription());
    }

    @Test
    void testFindByFieldOfStudyEmpty() {
        List<Scholarship> scholarships = scholarshipRepository.findByFieldOfStudy("NonExistentField");
        assertTrue(scholarships.isEmpty());
    }

    @Test
    void testFindByStateEmpty() {
        List<Scholarship> scholarships = scholarshipRepository.findByState("NonExistentState");
        assertTrue(scholarships.isEmpty());
    }

    @Test
    void testFindByDeadlineGreaterThanEmpty() {
        List<Scholarship> scholarships = scholarshipRepository.findByDeadlineGreaterThan(LocalDate.of(2027, 12, 31));
        assertTrue(scholarships.isEmpty());
    }
}
