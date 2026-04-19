package com.scholarship.backend.services;

import com.scholarship.backend.entities.Scholarship;
import com.scholarship.backend.repositories.ScholarshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScholarshipQueryServiceTest {

    @Mock
    private ScholarshipRepository scholarshipRepository;

    @InjectMocks
    private ScholarshipQueryService scholarshipQueryService;

    private Scholarship csScholarship;
    private Scholarship stemScholarship;
    private Scholarship artScholarship;

    @BeforeEach
    void setUp() {
        csScholarship = new Scholarship();
        csScholarship.setName("Google CS Scholarship");
        csScholarship.setDescription("For computer science students");
        csScholarship.setFieldOfStudy("Computer Science");
        csScholarship.setState("California");
        csScholarship.setEligibilityCriteria("GPA 3.5+");
        csScholarship.setAmount(10000.0);
        csScholarship.setDeadline(LocalDate.of(2027, 1, 1));

        stemScholarship = new Scholarship();
        stemScholarship.setName("NSF STEM Award");
        stemScholarship.setDescription("For students in STEM fields");
        stemScholarship.setFieldOfStudy("Engineering");
        stemScholarship.setState("National");
        stemScholarship.setEligibilityCriteria("Must be enrolled full time");
        stemScholarship.setAmount(5000.0);
        stemScholarship.setDeadline(LocalDate.of(2027, 6, 1));

        artScholarship = new Scholarship();
        artScholarship.setName("Arts Foundation Grant");
        artScholarship.setDescription("For students studying fine arts");
        artScholarship.setFieldOfStudy("Fine Arts");
        artScholarship.setState("New York");
        artScholarship.setEligibilityCriteria("Portfolio required");
        artScholarship.setAmount(3000.0);
        artScholarship.setDeadline(LocalDate.of(2027, 3, 1));
    }

    // --- getScholarshipById ---

    @Test
    void getScholarshipById_ReturnsScholarshipWhenFound() {
        when(scholarshipRepository.findById(1L)).thenReturn(Optional.of(csScholarship));

        Scholarship result = scholarshipQueryService.getScholarshipById(1L);

        assertNotNull(result);
        assertEquals("Google CS Scholarship", result.getName());
    }

    @Test
    void getScholarshipById_ReturnsNullWhenNotFound() {
        when(scholarshipRepository.findById(99L)).thenReturn(Optional.empty());

        Scholarship result = scholarshipQueryService.getScholarshipById(99L);

        assertNull(result);
    }

    // --- getAllScholarships ---

    @Test
    void getAllScholarships_ReturnsAllScholarships() {
        when(scholarshipRepository.findAll()).thenReturn(
            List.of(csScholarship, stemScholarship, artScholarship)
        );

        List<Scholarship> result = scholarshipQueryService.getAllScholarships();

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void getAllScholarships_ReturnsEmptyListWhenNoneExist() {
        when(scholarshipRepository.findAll()).thenReturn(List.of());

        List<Scholarship> result = scholarshipQueryService.getAllScholarships();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // --- searchScholarships ---

    @Test
    void searchScholarships_NullQuery_ReturnsAll() {
        when(scholarshipRepository.findAll()).thenReturn(
            List.of(csScholarship, stemScholarship, artScholarship)
        );

        List<Scholarship> result = scholarshipQueryService.searchScholarships(null);

        assertEquals(3, result.size());
    }

    @Test
    void searchScholarships_EmptyQuery_ReturnsAll() {
        when(scholarshipRepository.findAll()).thenReturn(
            List.of(csScholarship, stemScholarship, artScholarship)
        );

        List<Scholarship> result = scholarshipQueryService.searchScholarships("   ");

        assertEquals(3, result.size());
    }

    @Test
    void searchScholarships_MatchesByName() {
        when(scholarshipRepository.findAll()).thenReturn(
            List.of(csScholarship, stemScholarship, artScholarship)
        );

        List<Scholarship> result = scholarshipQueryService.searchScholarships("google");

        assertEquals(1, result.size());
        assertEquals("Google CS Scholarship", result.get(0).getName());
    }

    @Test
    void searchScholarships_MatchesByDescription() {
        when(scholarshipRepository.findAll()).thenReturn(
            List.of(csScholarship, stemScholarship, artScholarship)
        );

        List<Scholarship> result = scholarshipQueryService.searchScholarships("fine arts");

        assertEquals(1, result.size());
        assertEquals("Arts Foundation Grant", result.get(0).getName());
    }

    @Test
    void searchScholarships_MatchesByFieldOfStudy() {
        when(scholarshipRepository.findAll()).thenReturn(
            List.of(csScholarship, stemScholarship, artScholarship)
        );

        List<Scholarship> result = scholarshipQueryService.searchScholarships("engineering");

        assertEquals(1, result.size());
        assertEquals("NSF STEM Award", result.get(0).getName());
    }

    @Test
    void searchScholarships_MatchesByState() {
        when(scholarshipRepository.findAll()).thenReturn(
            List.of(csScholarship, stemScholarship, artScholarship)
        );

        List<Scholarship> result = scholarshipQueryService.searchScholarships("california");

        assertEquals(1, result.size());
        assertEquals("Google CS Scholarship", result.get(0).getName());
    }

    @Test
    void searchScholarships_MatchesByEligibilityCriteria() {
        when(scholarshipRepository.findAll()).thenReturn(
            List.of(csScholarship, stemScholarship, artScholarship)
        );

        List<Scholarship> result = scholarshipQueryService.searchScholarships("portfolio");

        assertEquals(1, result.size());
        assertEquals("Arts Foundation Grant", result.get(0).getName());
    }

    @Test
    void searchScholarships_IsCaseInsensitive() {
        when(scholarshipRepository.findAll()).thenReturn(
            List.of(csScholarship, stemScholarship, artScholarship)
        );

        List<Scholarship> result = scholarshipQueryService.searchScholarships("GOOGLE");

        assertEquals(1, result.size());
        assertEquals("Google CS Scholarship", result.get(0).getName());
    }

    @Test
    void searchScholarships_NoMatches_ReturnsEmptyList() {
        when(scholarshipRepository.findAll()).thenReturn(
            List.of(csScholarship, stemScholarship, artScholarship)
        );

        List<Scholarship> result = scholarshipQueryService.searchScholarships("xyznotfound");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void searchScholarships_MultipleMatches_ReturnsAll() {
        when(scholarshipRepository.findAll()).thenReturn(
            List.of(csScholarship, stemScholarship, artScholarship)
        );

        // "students" appears in all three descriptions
        List<Scholarship> result = scholarshipQueryService.searchScholarships("students");

        assertEquals(3, result.size());
    }

    // --- filterByField ---

    @Test
    void filterByField_ReturnsMatchingScholarships() {
        when(scholarshipRepository.findByFieldOfStudy("Computer Science"))
            .thenReturn(List.of(csScholarship));

        List<Scholarship> result = scholarshipQueryService.filterByField("Computer Science");

        assertEquals(1, result.size());
        assertEquals("Google CS Scholarship", result.get(0).getName());
    }

    @Test
    void filterByField_ReturnsEmptyListWhenNoMatches() {
        when(scholarshipRepository.findByFieldOfStudy("Philosophy"))
            .thenReturn(List.of());

        List<Scholarship> result = scholarshipQueryService.filterByField("Philosophy");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // --- filterByState ---

    @Test
    void filterByState_ReturnsMatchingScholarships() {
        when(scholarshipRepository.findByState("California"))
            .thenReturn(List.of(csScholarship));

        List<Scholarship> result = scholarshipQueryService.filterByState("California");

        assertEquals(1, result.size());
        assertEquals("Google CS Scholarship", result.get(0).getName());
    }

    @Test
    void filterByState_ReturnsEmptyListWhenNoMatches() {
        when(scholarshipRepository.findByState("Hawaii"))
            .thenReturn(List.of());

        List<Scholarship> result = scholarshipQueryService.filterByState("Hawaii");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}