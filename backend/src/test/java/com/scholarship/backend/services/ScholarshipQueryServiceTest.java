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

    @Test
    void getAllScholarships_ReturnsAllScholarships() {
        when(scholarshipRepository.findAll()).thenReturn(
                List.of(csScholarship, stemScholarship, artScholarship));

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

    @Test
    void searchScholarships_NullQuery_ReturnsAll() {
        when(scholarshipRepository.findAll()).thenReturn(
                List.of(csScholarship, stemScholarship, artScholarship));

        List<Scholarship> result = scholarshipQueryService.searchScholarships(null);

        assertEquals(3, result.size());
    }

    @Test
    void searchScholarships_EmptyQuery_ReturnsAll() {
        when(scholarshipRepository.findAll()).thenReturn(
                List.of(csScholarship, stemScholarship, artScholarship));

        List<Scholarship> result = scholarshipQueryService.searchScholarships("   ");

        assertEquals(3, result.size());
    }

    @Test
    void searchScholarships_MatchesByName() {
        when(scholarshipRepository.findAll()).thenReturn(
                List.of(csScholarship, stemScholarship, artScholarship));

        List<Scholarship> result = scholarshipQueryService.searchScholarships("google");

        assertEquals(1, result.size());
        assertEquals("Google CS Scholarship", result.get(0).getName());
    }

    @Test
    void searchScholarships_MatchesByDescription() {
        when(scholarshipRepository.findAll()).thenReturn(
                List.of(csScholarship, stemScholarship, artScholarship));

        List<Scholarship> result = scholarshipQueryService.searchScholarships("fine arts");

        assertEquals(1, result.size());
        assertEquals("Arts Foundation Grant", result.get(0).getName());
    }

    @Test
    void searchScholarships_MatchesByFieldOfStudy() {
        when(scholarshipRepository.findAll()).thenReturn(
                List.of(csScholarship, stemScholarship, artScholarship));

        List<Scholarship> result = scholarshipQueryService.searchScholarships("engineering");

        assertEquals(1, result.size());
        assertEquals("NSF STEM Award", result.get(0).getName());
    }

    @Test
    void searchScholarships_MatchesByState() {
        when(scholarshipRepository.findAll()).thenReturn(
                List.of(csScholarship, stemScholarship, artScholarship));

        List<Scholarship> result = scholarshipQueryService.searchScholarships("california");

        assertEquals(1, result.size());
        assertEquals("Google CS Scholarship", result.get(0).getName());
    }

    @Test
    void searchScholarships_MatchesByEligibilityCriteria() {
        when(scholarshipRepository.findAll()).thenReturn(
                List.of(csScholarship, stemScholarship, artScholarship));

        List<Scholarship> result = scholarshipQueryService.searchScholarships("portfolio");

        assertEquals(1, result.size());
        assertEquals("Arts Foundation Grant", result.get(0).getName());
    }

    @Test
    void searchScholarships_IsCaseInsensitive() {
        when(scholarshipRepository.findAll()).thenReturn(
                List.of(csScholarship, stemScholarship, artScholarship));

        List<Scholarship> result = scholarshipQueryService.searchScholarships("GOOGLE");

        assertEquals(1, result.size());
        assertEquals("Google CS Scholarship", result.get(0).getName());
    }

    @Test
    void searchScholarships_NoMatches_ReturnsEmptyList() {
        when(scholarshipRepository.findAll()).thenReturn(
                List.of(csScholarship, stemScholarship, artScholarship));

        List<Scholarship> result = scholarshipQueryService.searchScholarships("xyznotfound");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void searchScholarships_MultipleMatches_ReturnsAll() {
        when(scholarshipRepository.findAll()).thenReturn(
                List.of(csScholarship, stemScholarship, artScholarship));

        List<Scholarship> result = scholarshipQueryService.searchScholarships("students");

        assertEquals(3, result.size());

    }

    @Test
    void searchScholarships_NullNameDoesNotThrow() {
        Scholarship nullName = new Scholarship();
        nullName.setName(null);
        nullName.setDescription("Some description");

        when(scholarshipRepository.findAll()).thenReturn(List.of(nullName));

        List<Scholarship> result = scholarshipQueryService.searchScholarships("some");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void searchScholarships_NullDescriptionDoesNotThrow() {
        Scholarship nullDesc = new Scholarship();
        nullDesc.setName("Some Name");
        nullDesc.setDescription(null);

        when(scholarshipRepository.findAll()).thenReturn(List.of(nullDesc));

        List<Scholarship> result = scholarshipQueryService.searchScholarships("some");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void searchScholarships_NullFieldOfStudyDoesNotThrow() {
        Scholarship nullField = new Scholarship();
        nullField.setName("Some Name");
        nullField.setFieldOfStudy(null);

        when(scholarshipRepository.findAll()).thenReturn(List.of(nullField));

        List<Scholarship> result = scholarshipQueryService.searchScholarships("some");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void searchScholarships_NullStateDoesNotThrow() {
        Scholarship nullState = new Scholarship();
        nullState.setName("Some Name");
        nullState.setState(null);

        when(scholarshipRepository.findAll()).thenReturn(List.of(nullState));

        List<Scholarship> result = scholarshipQueryService.searchScholarships("some");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void searchScholarships_NullEligibilityCriteriaDoesNotThrow() {
        Scholarship nullEligibility = new Scholarship();
        nullEligibility.setName("Some Name");
        nullEligibility.setEligibilityCriteria(null);

        when(scholarshipRepository.findAll()).thenReturn(List.of(nullEligibility));

        List<Scholarship> result = scholarshipQueryService.searchScholarships("some");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void searchScholarships_AllFieldsNullDoesNotThrow() {
        Scholarship allNull = new Scholarship();

        when(scholarshipRepository.findAll()).thenReturn(List.of(allNull));

        List<Scholarship> result = scholarshipQueryService.searchScholarships("anything");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}