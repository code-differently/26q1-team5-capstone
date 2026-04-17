package com.scholarship.backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AIClient {

    @Value("${ai.api.key:demo-key}")
    private String apiKey;

    @Value("${ai.api.url:https://api.openai.com/v1/chat/completions}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public AIClient() {
        this.restTemplate = new RestTemplate();
    }

    public String getCompletion(String prompt) {
        // This is a mock implementation
        // In a real application, this would call an AI service like OpenAI

        // For demonstration, return a simple response
        return "1,2,3,4,5"; // Mock response indicating scholarship IDs 1,2,3,4,5 as matches

        // Real implementation would look something like:
        /*
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        requestBody.put("max_tokens", 150);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            Map<String, Object> choice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) choice.get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            // Fallback to simple matching
            return "1,2,3,4,5";
        }
        */
    }
}