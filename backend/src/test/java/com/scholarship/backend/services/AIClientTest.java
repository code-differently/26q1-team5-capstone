package com.scholarship.backend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AIClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AIClient aiClient;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(aiClient, "apiKey", "test-api-key-12345");
        ReflectionTestUtils.setField(aiClient, "apiUrl", "https://api.anthropic.com/v1/messages");
        ReflectionTestUtils.setField(aiClient, "restTemplate", restTemplate);
    }

    private ResponseEntity<String> buildResponse(String text) {
        String body = """
            {
              "content": [
                { "type": "text", "text": "%s" }
              ]
            }
            """.formatted(text);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Test
    void getCompletion_ReturnsTextFromResponse() {
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(buildResponse("Here are some scholarships for you."));

        String result = aiClient.getCompletion("Find me scholarships");

        assertNotNull(result);
        assertEquals("Here are some scholarships for you.", result);
    }

    @Test
    void getCompletion_CallsApiWithPrompt() {
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(buildResponse("response"));

        aiClient.getCompletion("test prompt");

        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void getCompletion_ReturnsEmptyJsonArrayOnException() {
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("Connection refused"));

        String result = aiClient.getCompletion("test prompt");

        assertEquals("[]", result);
    }

    @Test
    void getCompletion_ReturnsEmptyJsonArrayOnMalformedResponse() {
        ResponseEntity<String> badResponse = new ResponseEntity<>("not valid json", HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(badResponse);

        String result = aiClient.getCompletion("test prompt");

        assertEquals("[]", result);
    }

    @Test
    void getCompletion_HandlesMultilinePrompt() {
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(buildResponse("Scholarship list here."));

        String result = aiClient.getCompletion("Line one\nLine two\nLine three");

        assertNotNull(result);
        assertEquals("Scholarship list here.", result);
    }
}