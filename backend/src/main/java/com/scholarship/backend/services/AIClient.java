package com.scholarship.backend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AIClient {

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.url:https://api.anthropic.com/v1/messages}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AIClient() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String getCompletion(String prompt) {
    try {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "claude-haiku-4-5-20251001");
        requestBody.put("max_tokens", 2000);
        requestBody.put("messages", List.of(
            Map.of("role", "user", "content", prompt)
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        System.out.println("=== AI REQUEST ===");
        System.out.println("URL: " + apiUrl);
        System.out.println("Key starts with: " + (apiKey != null ? apiKey.substring(0, 10) : "NULL"));

        ResponseEntity<String> response =
            restTemplate.postForEntity(apiUrl, request, String.class);

        System.out.println("=== AI RESPONSE ===");
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Body: " + response.getBody());

        Map<String, Object> responseMap =
            objectMapper.readValue(response.getBody(), Map.class);

        List<Map<String, Object>> content =
            (List<Map<String, Object>>) responseMap.get("content");

        return (String) content.get(0).get("text");

    } catch (Exception e) {
        System.err.println("=== AI ERROR ===");
        System.err.println("Message: " + e.getMessage());
        e.printStackTrace();
        return "[]";
    }
}
    
}